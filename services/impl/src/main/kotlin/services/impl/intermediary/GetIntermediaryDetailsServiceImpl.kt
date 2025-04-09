package services.impl.intermediary

import models.intermediary.IntermediaryModel
import repository.api.repository.IntermediaryRepository
import services.api.services.intermediary.GetIntermediaryDetailsService
import services.api.services.user.GetUserByIdService
import utils.exceptions.http.notFoundError
import utils.exceptions.http.unauthorizedResourceError
import java.util.*

internal class GetIntermediaryDetailsServiceImpl(
    private val intermediaryRepository: IntermediaryRepository,
    private val getUserByIdService: GetUserByIdService
) : GetIntermediaryDetailsService {

    override suspend fun getIntermediaryDetails(userId: UUID, intermediaryId: UUID): IntermediaryModel {
        val user = getUserByIdService.get(userId)

        val intermediary = intermediaryRepository.getById(intermediaryId = intermediaryId)
            ?: notFoundError("Intermediary not found")

        if (intermediary.userId != user.id) {
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