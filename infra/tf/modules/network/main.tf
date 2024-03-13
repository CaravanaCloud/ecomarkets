resource "aws_vpc" "that" {
  cidr_block = "10.0.0.0/16"
  enable_dns_support = true
  enable_dns_hostnames = true

  tags = {
    Name = "networking"
    EnvId = var.env_id
  }
}

locals {
  private_subnet_cidr = ["10.0.1.0/24", "10.0.3.0/24", "10.0.5.0/24"]
  public_subnet_cidr  = ["10.0.2.0/24", "10.0.4.0/24", "10.0.6.0/24"]
}

data "aws_availability_zones" "available" {
  state = "available"
}

resource "aws_subnet" "private" {
  count = length(local.private_subnet_cidr)
  vpc_id = aws_vpc.that.id
  cidr_block = local.private_subnet_cidr[count.index]
  availability_zone = data.aws_availability_zones.available.names[count.index]
  map_public_ip_on_launch = false

  tags = {
    Name = "private-${count.index}"
  }
}

resource "aws_subnet" "public" {
  count = length(local.public_subnet_cidr)
  vpc_id = aws_vpc.that.id
  cidr_block = local.public_subnet_cidr[count.index]
  availability_zone = data.aws_availability_zones.available.names[count.index]
  map_public_ip_on_launch = true

  tags = {
    Name = "public-${count.index}"
  }
}

resource "aws_internet_gateway" "that" {
  vpc_id = aws_vpc.that.id
  tags = {
    Name = "igw"
  }
}

resource "aws_route_table" "public" {
  vpc_id = aws_vpc.that.id
  tags = {
    Name = "public-rt"
  }
}

resource "aws_route_table_association" "public_assoc" {
  count = length(local.public_subnet_cidr)
  subnet_id = aws_subnet.public[count.index].id
  route_table_id = aws_route_table.public.id
}

resource "aws_route" "public_internet" {
  route_table_id = aws_route_table.public.id
  destination_cidr_block = "0.0.0.0/0"
  gateway_id = aws_internet_gateway.that.id
}
