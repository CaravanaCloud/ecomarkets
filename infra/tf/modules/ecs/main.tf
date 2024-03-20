# ECS Cluster
resource "aws_ecs_cluster" "that" {
  name = "${var.env_id}-ecs"
}

# IAM Role for ECS Task Execution
resource "aws_iam_role" "ecs_task_execution_role" {
  name = "ecs_task_execution_role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Action = "sts:AssumeRole",
        Effect = "Allow",
        Principal = {
          Service = "ecs-tasks.amazonaws.com"
        },
      },
    ],
  })
}

resource "aws_iam_role_policy_attachment" "ecs_task_execution_role_policy" {
  role       = aws_iam_role.ecs_task_execution_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

resource "aws_iam_policy" "ecs_secrets_policy" {
  name        = "ecs-secrets-policy"
  description = "Allow ECS Task Execution Role to retrieve secrets"

  policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Action = [
          "secretsmanager:GetSecretValue",
        ],
        Resource = [
          "arn:aws:secretsmanager:us-west-2:123456789012:secret:DockerRegistryCredentials", # The ARN of your secret
        ],
        Effect = "Allow",
      },
    ],
  })
}

resource "aws_iam_role_policy_attachment" "ecs_secrets_policy_attach" {
  role       = aws_iam_role.ecs_task_execution_role.name
  policy_arn = aws_iam_policy.ecs_secrets_policy.arn
}

# Task Definition
resource "aws_ecs_task_definition" "that" {
  family                   = "task-family"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  execution_role_arn       = aws_iam_role.ecs_task_execution_role.arn
  cpu                      = "256"
  memory                   = "512"

  container_definitions = jsonencode([
    {
      name      = "container",
      image     = "nginx",
      cpu       = 256,
      memory    = 512,
      essential = true,
      
      portMappings = [
        {
          containerPort = 80,
          hostPort      = 80,
          protocol      = "tcp"
        },
      ],

      logConfiguration = {
        logDriver = "awslogs"
        options = {
          awslogs-group         = "/ecs/task-logs"
          awslogs-region        = "${var.aws_region}"
          awslogs-stream-prefix = "ecs" 
        }
      }
    },
  ])
}

resource "aws_security_group" "ecs_sg" {
  name        = "ecs-security-group"
  description = "Allow inbound traffic from VPC"
  vpc_id      = var.vpc_id

  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"] # TODO 
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
  tags = {
    Name = "aurora-security-group"
  }
}

# ECS Service
resource "aws_ecs_service" "that" {
  name            = "service"
  cluster         = aws_ecs_cluster.that.id
  task_definition = aws_ecs_task_definition.that.arn
  launch_type     = "FARGATE"

  network_configuration {
    subnets          = var.ecs_subnets
    security_groups  = [ aws_security_group.ecs_sg.id]
    assign_public_ip = true
  }

  desired_count = 1
}

