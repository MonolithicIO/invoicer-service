package services.impl.intermediary

import models.intermediary.IntermediaryModel
import repository.api.repository.IntermediaryRepository
import services.api.services.intermediary.GetIntermediaryDetailsService
import services.api.services.user.GetUserByIdService
import utils.exceptions.notFoundError
import utils.exceptions.unauthorizedResourceError
import java.util.*

internal class GetIntermediaryDetailsServiceImpl(
    private val intermediaryRepository: IntermediaryRepository,
    private val getUserByIdService: GetUserByIdService
) : GetIntermediaryDetailsService {

    override suspend fun getIntermediaryDetails(userId: String, intermediaryId: String): IntermediaryModel {
        val user = getUserByIdService.get(userId)

        val intermediary = intermediaryRepository.getById(intermediaryId = UUID.fromString(intermediaryId))
            ?: notFoundError("Intermediary not found")

        if (intermediary.userId != user.id.toString()) {
            unauthorizedResourceError()
        }

        return IntermediaryModel(
            name = intermediary.name,
            iban = intermediary.iban,
            swift = intermediary.swift,
            bankName = intermediary.bankName,
            bankAddress = intermediary.bankAddress,
            userId = intermediary.userId,
            id = intermediary.id,
            createdAt = intermediary.createdAt,
            updatedAt = intermediary.updatedAt
        )
    }
}