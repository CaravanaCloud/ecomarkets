package ecomarkets.rs.basket.form;

import java.util.List;

import ecomarkets.core.domain.core.fair.FairId;
import ecomarkets.core.domain.core.partner.PartnerId;

public record CreateBasketForm (FairId fairId, PartnerId partnerId, List<BasketItemForm> items){}
