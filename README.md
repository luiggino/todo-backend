# Postgres
docker run --name todo-postgres -e POSTGRES_PASSWORD=mysecretpassword -d postgres:14.5-alpine

docker run --name postgresql -e POSTGRES_USER=todo -e POSTGRES_PASSWORD=mysecretpassword -p 5432:5432 -d postgres:14.5-alpine