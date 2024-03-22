output env_id {
    value = var.env_id
}
output aws_region {
    value = var.aws_region
}
output vpc_id {
    value = module.network.vpc_id
}

output public_subnet_ids {
    value = module.network.public_subnet_ids
}

output public_subnet_ids_str {
    value =  join(",",module.network.public_subnet_ids)
}

output private_subnet_ids {
    value = module.network.private_subnet_ids
}

output private_subnet_ids_str {
    value =  join(",", module.network.private_subnet_ids)
}

output storage_bucket_name {
    value = module.storage.bucket_name
}

output db_endpoint {
    value = module.database.db_endpoint
}

output db_name {
    value = module.database.db_name
}


