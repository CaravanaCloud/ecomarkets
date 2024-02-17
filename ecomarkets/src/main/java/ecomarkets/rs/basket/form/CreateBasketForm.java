package ecomarkets.rs.basket.form;

import ecomarkets.domain.core.fair.FairId;
import ecomarkets.domain.core.partner.PartnerId;

import java.util.List;

public record CreateBasketForm (FairId fairId, PartnerId partnerId, List<BasketItemForm> items){}
