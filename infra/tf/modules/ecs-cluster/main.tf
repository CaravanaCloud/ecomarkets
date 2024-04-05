resource "aws_ecs_cluster" "that" {
  name = "${var.env_id}-ecs"
}