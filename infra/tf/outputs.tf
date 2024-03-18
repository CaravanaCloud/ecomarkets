output storage_bucket_name {
    value = module.storage.bucket_name
}

output db_endpoint {
    value = module.database.db_endpoint
}

output api_endpoint {
    value = module.api.api_endpoint
}