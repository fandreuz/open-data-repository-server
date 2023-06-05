# (ROOT) data server

RESTful web service for storage and metadata extraction of Open Data repositories.

## Technologies

### Software stack

- Java 11
- Quarkus
	- RESTEasy Reactive
	- Jackson extension
- Hibernate Validator
- Jakarta EE CDI
- MongoDB Java driver
- Apache Commons CSV
- Apache Commons Lang3
- Slf4j + Logback

### Build tools & plugins

- Quarkus CLI
- Gradle (Kotlin DSL)
- Spotless
- Lombok

## Model objects

### `DatasetMetadata`

TODO

### Dataset ID

TODO

## REST endpoints

### `PUT /`

Idempotent creation of a new dataset in the database.

Example request body:

```json
{
    "collectionId": "211",
    "fileName": "qcd.root"
}
```

The locator above will trigger the creation of [this](http://opendata.cern.ch/record/211/files/qcd.root) dataset in the
database. If the operation succeeds the endpoint will return a JSON representation of `DatasetMetadata` representing the
imported dataset.

### `GET /metadata/{id}`

The given `{id}` is used to locate metadata for an imported dataset. If found, the JSON representation of the
appropriate `DatasetMetadata` is returned.

### `GET /metadata`

Return a sorted collection of all metadata objects stored in the database.

## TODO

- [x] CRU~~D~~ operations for datasets
  - [x] Idempotent create (update not needed)
  - [x] Thread-safe create endpoint
  - [x] Import CSV datasets
  - [x] Import ROOT datasets
- [ ] Endpoints for simple calculations
- [ ] Endpoints for simple querying
  - [x] Endpoint to get column names
- [ ] Data lifecycle
- [ ] Docker image
- [ ] ~~Tests~~
