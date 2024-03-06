package ecomarkets.rs.fair;

import ecomarkets.domain.core.farmer.FarmerId;

public record AddProductForm(FarmerId farmerId, Integer amount) {
}
