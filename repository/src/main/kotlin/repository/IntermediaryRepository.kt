package repository

import datasource.api.database.IntermediaryDatabaseSource
import datasource.api.model.intermediary.CreateIntermediaryData
import datasource.api.model.intermediary.UpdateIntermediaryData
import foundation.cache.CacheHandler
import models.intermediary.CreateIntermediaryModel
import models.intermediary.IntermediaryModel
import models.intermediary.PartialUpdateIntermediaryModel
import java.util.*

interface IntermediaryRepository {
    suspend fun create(
        userId: UUID,
        model: CreateIntermediaryModel
    ): String

    suspend fun delete(
        userId: UUID,
        intermediaryId: UUID
    )

    suspend fun getById(
        intermediaryId: UUID
    ): IntermediaryModel?

    suspend fun getBySwift(
        userId: UUID,
        swift: String
    ): IntermediaryModel?

    suspend fun getAll(
        userId: UUID,
        page: Long,
        limit: Int,
    ): List<IntermediaryModel>

    suspend fun update(
        userId: UUID,
        intermediaryId: UUID,
        model: PartialUpdateIntermediaryModel
    ): IntermediaryModel
}

internal class IntermediaryRepositoryImpl(
    private val databaseSource: IntermediaryDatabaseSource,
    private val cacheHandler: CacheHandler
) : IntermediaryRepository {

    override suspend fun create(userId: UUID, model: CreateIntermediaryModel): String {
        return databaseSource.create(
            userId = userId,
            model = CreateIntermediaryData(
                name = model.name,
                iban = model.iban,
                swift = model.swift,
                bankName = model.bankName,
                bankAddress = model.bankAddress
            )
        )
    }

    override suspend fun delete(userId: UUID, intermediaryId: UUID) {
        return databaseSource.delete(
            userId = userId,
            intermediaryId = intermediaryId
        ).also {
            cacheHandler.delete(intermediaryId.toString())
        }
    }

    override suspend fun getById(intermediaryId: UUID): IntermediaryModel? {
        val cached = cacheHandler.get(
            key = intermediaryId.toString(),
            serializer = IntermediaryModel.serializer()
        )

        if (cached != null) return cached

        val databaseResult = databaseSource.getById(intermediaryId)

        return databaseResult?.also {
            cacheHandler.set(
                key = intermediaryId.toString(),
                value = it,
                serializer = IntermediaryModel.serializer(),
                ttlSeconds = CACHE_TTL_SECONDS
            )
        }
    }

    override suspend fun getBySwift(userId: UUID, swift: String): IntermediaryModel? {
        return databaseSource.getBySwift(
            userId = userId,
            swift = swift
        )
    }

    override suspend fun getAll(
        userId: UUID,
        page: Long,
        limit: Int
    ): List<IntermediaryModel> {
        return databaseSource.getAll(
            userId = userId,
            page = page,
            limit = limit
        )
    }

    override suspend fun update(
        userId: UUID,
        intermediaryId: UUID,
        model: PartialUpdateIntermediaryModel
    ): IntermediaryModel {
        return databaseSource.update(
            userId = userId,
            intermediaryId = intermediaryId,
            model = UpdateIntermediaryData(
                name = model.name,
                iban = model.iban,
                swift = model.swift,
                bankName = model.bankName,
                bankAddress = model.bankAddress
            )
        ).also {
            cacheHandler.delete(intermediaryId.toString())
        }
    }

    companion object {
        const val CACHE_TTL_SECONDS = 600L
    }
}