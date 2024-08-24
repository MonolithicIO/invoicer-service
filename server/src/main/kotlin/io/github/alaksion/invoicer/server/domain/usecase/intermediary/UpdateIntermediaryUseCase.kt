package io.github.alaksion.invoicer.server.domain.usecase.intermediary

import io.github.alaksion.invoicer.server.domain.model.beneficiary.BeneficiaryModel
import io.github.alaksion.invoicer.server.domain.model.intermediary.IntermediaryModel
import io.github.alaksion.invoicer.server.domain.model.intermediary.UpdateIntermediaryModel
import io.github.alaksion.invoicer.server.domain.repository.IntermediaryRepository
import io.github.alaksion.invoicer.server.domain.usecase.user.GetUserByIdUseCase
import io.github.alaksion.invoicer.server.validation.validateSwiftCode
import io.ktor.http.HttpStatusCode
import utils.exceptions.httpError
import java.util.UUID

interface UpdateIntermediaryUseCase {
    suspend fun execute(
        model: UpdateIntermediaryModel,
        userId: String,
        intermediaryId: String
    ): IntermediaryModel
}

internal class UpdateIntermediaryUseCaseImpl(
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val getIntermediaryByIdUseCase: GetIntermediaryByIdUseCase,
    private val checkIntermediarySwiftAlreadyUsedUseCaseImpl: CheckIntermediarySwiftAvailableUseCase,
    private val intermediaryRepository: IntermediaryRepository
) : UpdateIntermediaryUseCase {

    override suspend fun execute(
        model: UpdateIntermediaryModel,
        userId: String,
        intermediaryId: String
    ): IntermediaryModel {
        if (validateSwiftCode(model.swift).not()) {
            httpError("Invalid swift code: ${model.swift}", HttpStatusCode.BadRequest)
        }

        getUserByIdUseCase.get(userId)

        getIntermediaryByIdUseCase.get(
            intermediaryId = intermediaryId,
            userId = userId
        )

        if (checkIntermediarySwiftAlreadyUsedUseCaseImpl.execute(
                swift = model.swift,
                userId = userId
            )
        ) {
            httpError(
                message = "Swift code: ${model.swift} is already in use by another beneficiary",
                code = HttpStatusCode.Conflict
            )
        }

        return intermediaryRepository.update(
            userId = UUID.fromString(userId),
            intermediaryId = UUID.fromString(intermediaryId),
            model = model
        )
    }

}