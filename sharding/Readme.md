# Sharding

Para correr el docker y que todo funcione en su totalidad se deben seguir estos pasos:

### 1. Levantar el contenedor desde este directorio
```bash
docker compose up -d
```

### 2. Levantar los servers de configuración
```bash
docker compose exec configsvr01 sh -c "mongosh < /scripts/init-configserver.js"
```

### 3. Levantar los servers de shard
```bash
docker compose exec shard01-a sh -c "mongosh < /scripts/init-shard01.js"
docker compose exec shard02-a sh -c "mongosh < /scripts/init-shard02.js"
```

### 4. Levantar el router
```bash
docker compose exec router01 sh -c "mongosh < /scripts/init-router.js"
```

Si todo salio bien, ya estaría todo listo para usar el cluster de sharding. Pero antes esta bueno verificar si todo se configuro correctamente, para ello:

### 5. Verificación
```bash
docker compose exec router01 mongosh --port 27017
```
y luego
```javascript
sh.status()
```

## Todos los comandos
En total estos serian los comandos a ejecutar
```bash
docker compose up -d
docker compose exec configsvr01 sh -c "mongosh < /scripts/init-configserver.js"
docker compose exec shard01-a sh -c "mongosh < /scripts/init-shard01.js"
docker compose exec shard02-a sh -c "mongosh < /scripts/init-shard02.js"
docker compose exec router01 sh -c "mongosh < /scripts/init-router.js"
```

Lo que podriamos aplicar a un _.sh_ en un futuro.

# Coneccion a Studio 3T

Para conectarse a la base en Studio 3T se debe pegar el siguiente link.
```
mongodb://localhost:27117,localhost:27118/?retryWrites=true&serverSelectionTimeoutMS=5000&connectTimeoutMS=10000
```

# Estructura de archivos
Se agregaran los scripts necesarios para la entrega en el directorio scripts.


# En caso de que no funcionen los comandos docker compose exec
Habrias que hacerlo mas a mano solamente una vez mientras no borren el docker

### 1. Levantar el contenedor desde este directorio
```bash
docker compose up -d
```

### 2. Levantar los servers de configuración
```bash
docker exec -it mongo-config-01 bash
mongosh < /scripts/init-configserver.js
```

### 3. Levantar los servers de shard

#### Shard 1
```bash
docker exec -it shard-01-node-a bash
mongosh < /scripts/init-shard01.js
```

#### Shard 2
```bash
docker exec -it shard-02-node-a bash
mongosh < /scripts/init-shard02.js
```

### 4. Levantar el router
```bash
docker exec -it router-01 bash
mongosh < /scripts/init-router.js
```

Se verifica de la misma manera que antes
