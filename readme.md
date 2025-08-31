# Launch the kafka servers with docker compose command

<code>docker-compose --env-file environment.env up -d
</code>

# Create a product

```
POST http://localhost:8080/products</code
{
  "title": "Sample Product",
  "price": 100,
  "description": 19
}
```