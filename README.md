
## API endpoints

#### EVERY /instance :id
        return
            401 // user unauthorized
            403 // access to instance forbidden
            404 // instance not found

#### POST /instance
    spawn calculations instance
        body - settings
    return
        200 {id: id}
        400 // bad settings
        500 // failed
        503 // too many instances

#### GET /instance
    get list of all instances owned by user
        array of objects like:
        {
            id: "instance id",
            created: sql datetime string or Date() object,
            ?completed: sql datetime string or Date() object,
            name: instance name or null,
            settings: Object with instance settings
        }
        completed === undefined => instance is still calculating
    return
        200 {instances: [...]}

#### GET /instance/:id
    get instance data
    status
        'processing'- calculating things
        'failed'    - Cutoff process dead
        'completed' - completed
        'zombie'    - results lost
    return
        200 {
            status,
            ?percentage,
            ?data: { // can be null if something breaks
                particles: [[energy, status, time], ...],
                lower,
                upper,
                effective
            }
        }

#### GET /instance/:id/:trace (where :trace is energy)
    get trace data (calculate if not exist)m can take some seconds
    return
        102 // instance or another processing
        200 [[t, x, y, z],...]
        500 // failed

#### POST /instance/:id/kill
    stop processing instance and remove it
    return
        200 // dead

#### POST /instance/:id/name
    with body: {name: "new name up to 255 chars"}
    return
        200 // renamed
        500 // failed



#### POST /user
    body {email: "", password: ""}
    register
    return
        201 // user created
        400 // no user data provided
        409 // email already occupied

#### POST /user/login
    body {email: "", password: ""} or {guest: true}
    login as user or as guest
    return
        200 // logged in as {}
        400 // wrong password
        404

#### POST /user/logout
    return
        200 // logged out

#### GET /user/
    return
        200 with json {login: bool, [guest: bool, username: string]}
        404 // ???

## application.properties
spring.jpa.hibernate.ddl-auto=none
spring.datasource.url=jdbc:mysql://${MYSQL_HOST:localhost}:3306/db_name
spring.datasource.username=
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.jdbc.Driver

## MySQL structure

```sql
CREATE TABLE IF NOT EXISTS `users` (
      `id` int NOT NULL AUTO_INCREMENT,
      `email` varchar(255) NOT NULL,
      `password` BINARY(60) NOT NULL,
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `instances` (
  `id` CHAR(36) NOT NULL,
  `owner` int NOT NULL,
  `name` varchar(255),
  `created` DATETIME,
  `completed` DATETIME,
  `datetime` DATETIME,
  `swdp` FLOAT,
  `dst` FLOAT,
  `imfBy` FLOAT,
  `imfBz` FLOAT,
  `g1` FLOAT,
  `g2` FLOAT,
  `kp` FLOAT,
  `model` CHAR(2),
  `alt` FLOAT,
  `lat` FLOAT,
  `lon` FLOAT,
  `vertical` FLOAT,
  `azimutal` FLOAT,
  `lower` FLOAT,
  `upper` FLOAT,
  `step` FLOAT,
  `flight_time` FLOAT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;
```
