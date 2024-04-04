package ecomarkets.rs.fair;

import ecomarkets.core.domain.core.farmer.FarmerId;

public record AddProductForm(FarmerId farmerId, Integer amount) {
}
