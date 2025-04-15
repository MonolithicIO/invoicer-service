package services.impl.intermediary

import repository.IntermediaryRepository
import services.api.services.intermediary.CheckIntermediarySwiftAvailableService
import java.util.*

internal class CheckIntermediarySwiftAvailableServiceImpl(
    private val repository: _root_ide_package_.repository.IntermediaryRepository
) : CheckIntermediarySwiftAvailableService {

    override suspend fun execute(swift: String, userId: UUID): Boolean {
        val response =
            repository.getBySwift(
                userId = userId,
                swift = swift
            ) != null

        return response
    }
}