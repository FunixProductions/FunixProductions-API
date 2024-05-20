package com.funixproductions.api.payment.billing.service.resources;

import com.funixproductions.api.payment.billing.client.clients.BillingClient;
import com.funixproductions.api.payment.billing.client.dtos.BillingDTO;
import com.funixproductions.api.payment.billing.service.services.BillingCrudService;
import com.funixproductions.api.user.client.dtos.UserDTO;
import com.funixproductions.api.user.client.security.CurrentSession;
import com.funixproductions.core.crud.dtos.PageDTO;
import com.funixproductions.core.crud.enums.SearchOperation;
import com.funixproductions.core.exceptions.ApiForbiddenException;
import com.funixproductions.core.exceptions.ApiUnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/billing/user")
@RequiredArgsConstructor
public class BillingUserResource implements BillingClient {

    private final CurrentSession currentSession;
    private final BillingCrudService billingCrudService;

    @Override
    public PageDTO<BillingDTO> getAll(final String page,
                                      final String elemsPerPage,
                                      final String search,
                                      final String sort) {
        final UserDTO userDTO = currentSession.getCurrentUser();
        if (userDTO == null || userDTO.getId() == null) {
            throw new ApiUnauthorizedException("Utilisateur non connecté.");
        }

        final String overrideSearch = String.format("billedEntityFunixProdId:%s:%s", SearchOperation.EQUALS.getOperation(), userDTO.getId().toString());
        return billingCrudService.getAll(page, elemsPerPage, overrideSearch, sort);
    }

    @Override
    public BillingDTO findById(final String id) {
        final UserDTO userDTO = currentSession.getCurrentUser();
        if (userDTO == null || userDTO.getId() == null || userDTO.getRole() == null) {
            throw new ApiUnauthorizedException("Utilisateur non connecté ou non disponible sur l'api.");
        }
        final BillingDTO billingDTO = billingCrudService.findById(id);

        if (userDTO.getId().toString().equals(billingDTO.getBilledEntity().getUserFunixProdId())) {
            return billingDTO;
        } else {
            log.info("Utilisateur id : {} nom : {} email : {} n'a pas accès à la facture id {}", userDTO.getId(), userDTO.getUsername(), userDTO.getEmail(), id);
            throw new ApiForbiddenException("Vous n'avez pas accès à cette facture.");
        }
    }

    @Override
    public Resource downloadInvoice(final String id) {
        final UserDTO userDTO = currentSession.getCurrentUser();
        if (userDTO == null) {
            throw new ApiUnauthorizedException("Utilisateur non connecté.");
        }

        return billingCrudService.getInvoiceFile(id);
    }
}
