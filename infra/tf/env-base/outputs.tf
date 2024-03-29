
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

output "infra_bucket_name" {
    value = module.infra_storage.bucket_name
}

output "db_endpoint" {
    value = module.database.db_endpoint
}

output "db_name" {
    value = module.database.db_name
}


output "certificate_arn" {
    value = module.domain.certificate_arn
}

output "name_servers" {
    value = module.domain.name_servers
}

output "zone_id" {
    value = module.domain.zone_id
}

output "domain_name" {
    value = module.domain.domain_name
}
