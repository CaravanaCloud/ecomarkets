# Outputs
output "cluster_endpoint" {
  description = "Endpoint for EKS cluster"
  value       = aws_eks_cluster.that.endpoint
}

output "cluster_id" {
  description = "EKS cluster ID"
  value       = aws_eks_cluster.that.id
}

output "cluster_name" {
  description = "EKS cluster name"
  value       = aws_eks_cluster.that.name
}

output "cluster_role_arn" {
  description = "EKS cluster role ARN"
  value       = aws_eks_cluster.that.role_arn
}
