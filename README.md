# Open data server

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
- JSoup
- Slf4j + Logback
- Swagger (OpenAPI)

### Build tools & plugins

- Quarkus CLI
- Gradle (Kotlin DSL)
- Spotless
- Lombok
- Docker

## Abstract code structure

The following schema summarizes the interconnection of the interfaces defined in the public section of each main
package (`model`, `controller`, `database`, `fetch`, `conversion`):

![](https://plantuml-server.kkeisuke.dev/svg/VLJBRi8m4BpdAwpUGzKU4K9KgL2f-b9BUwbwCFOI8eoDx0MfGltthaCCmJ4S4E2P7S_ERZ9oo2rkLYfJC4U6XjcgN22JbGM1bT5PjkPYoKjWmcYqHYcmR9Snvd6kImNidYDtWE_WpCOAI67FW5pIpnPdRfGagORmP0H7Ozat8OmDPiFJyy7rR5WZUPxNty8RgGrEP7qmhnIyy9LN_g5FCBtbggABYTT8J_HwWr-7qm-msqh4LII64CnyEh1t9MWSxqzhxjynbvMHeDAHXBPJM66CbPNc28vW6eDO1cZwkpvDiJXqUqanzDBoTkMvCmAl8eDJoxNZjMHnc6j7r5TwCx9G5GNGLgPjs89rFjXTv3K0nsmrHTG5NgrOW4DxElYBjCuU56wRkf2nHrTtba1wlLuymZcWM4HzXAIZBfginxwYWJfBsmOxZdsUDrtnFN2R0bg6mpXfwNHfv2pB-XjQppxBloLt2v0_akuPneyawxE7wVJjCZb-HaDH5elbzbWKV9xJI76pq_y-cOOKxrkIc5xTivhHFDB4YqktDnnvstTs6CC8jAItw3y0.svg)

## Model objects

The following models are defined in the package `io.github.fandreuz.root.data.server.models`:

### `CollectionMetadata`

Metadata for a collection contains metadata related to the experiment which generated the collection of datasets. It
contains compulsory [DataCite](https://schema.datacite.org/) information and a subset of the recommended ones.

**Example**:

```json
{
  "id": "cern-open-data:13128",
  "name": "13128",
  "experimentName": "OPERA",
  "eventsCount": 1,
  "type": "Derived",
  "keyword": "",
  "tag": "CERN-SPS",
  "citeText": "Cite as: OPERA collaboration (2019). OPERA neutrino-induced charmed hadron event 237040910. CERN Open Data Portal. ",
  "doi": "10.7483/OPENDATA.OPERA.Q74R.SYBQ",
  "license": "Creative Commons CC0 waiver",
  "creator": "curl/7.76.1",
  "title": "OPERA neutrino-induced charmed hadron event 237040910",
  "publisher": "OPERA",
  "publicationYear": 2019,
  "language": "English",
  "subject": "High Energy Physics, Theoretical Physics",
  "description": "This OPERA detector event is a muon neutrino interaction with the lead target where a charmed hadron was reconstructed in the final state. The event data consist of Electronic Detector files (such as Drift Tube, RPC, and Target Tracker files) and Emulsion Detector files (such as Tracks and Vertex files). For more information, see the description of the whole dataset.",
  "geoLocation": "46.233832398 6.053166454",
  "fundingReference": "https://perma.cc/L34T-TCTG"
}
```

### `DatasetMetadata`

Dataset metadata contain metadata strictly related to the dataset file, and wrap the metadata of the collection the
dataset belongs to.

**Example**:

```json
{
  "datasetId": "cern-open-data:13128:237040910_EventInfo",
  "fileName": "237040910_EventInfo.csv",
  "type": "CSV",
  "sizeInBytes": 52,
  "numberOfColumns": 3,
  "commaSeparatedColumnNames": "evID,timestamp,muMom",
  "importTimestamp": 1686615824740,
  "collectionMetadata": {
    ...
  }
}
```

## Unique identifiers

Uniform Resource Name (URN) standard: `schema:namespace:resourceName`

### Dataset ID

A dataset is identified by the schema, the namespace name (i.e. the collection it belongs to) and the file name.

### Example

We identify the file `experimentData` in the collection `19090` with the following URN:

```
cern-open-data:19090:experimentData
```

### Collection ID

A dataset is identified by the schema and its name. The last part of the URN is omitted since it's not needed.

### Example

We identify the collection `19090` with the following URN:

```
cern-open-data:19090
```

### Consequences

We can infer the collection URN based on the dataset URN by removing the trailing part.

## REST endpoints

### `PUT /v1`

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

Sample interaction:

```
curl --header "Content-Type: application/json" \
    --request PUT \
    --data '{"collectionId":"13128","fileName":"237040910_EventInfo.csv"}' \
    http://localhost:8080/v1
```

```json
{
  "datasetId": "cern-open-data:13128:237040910_EventInfo",
  "fileName": "237040910_EventInfo.csv",
  "type": "CSV",
  "sizeInBytes": 52,
  "numberOfColumns": 3,
  "commaSeparatedColumnNames": "evID,timestamp,muMom",
  "importTimestamp": 1686615824740,
  "collectionMetadata": {
    "id": "cern-open-data:13128",
    "name": "13128",
    "experimentName": "OPERA",
    "eventsCount": 1,
    "type": "Derived",
    "keyword": "",
    "tag": "CERN-SPS",
    "citeText": "Cite as: OPERA collaboration (2019). OPERA neutrino-induced charmed hadron event 237040910. CERN Open Data Portal. ",
    "doi": "10.7483/OPENDATA.OPERA.Q74R.SYBQ",
    "license": "Creative Commons CC0 waiver",
    "creator": "curl/7.76.1",
    "title": "OPERA neutrino-induced charmed hadron event 237040910",
    "publisher": "OPERA",
    "publicationYear": 2019,
    "language": "English",
    "subject": "High Energy Physics, Theoretical Physics",
    "description": "This OPERA detector event is a muon neutrino interaction with the lead target where a charmed hadron was reconstructed in the final state. The event data consist of Electronic Detector files (such as Drift Tube, RPC, and Target Tracker files) and Emulsion Detector files (such as Tracks and Vertex files). For more information, see the description of the whole dataset.",
    "geoLocation": "46.233832398 6.053166454",
    "fundingReference": "https://perma.cc/L34T-TCTG"
  }
}
```

### `GET /v1/{id}/{column-name}`

Get the content of a column for the given dataset.

Sample interaction:

```
curl -i --request GET \
    http://localhost:8080/v1/cern-open-data:13128:237040910_EventInfo/posX
```

```json
{
  "647fa76f10c98516828586ae": "66.55",
  "647fa76f10c98516828586af": "64.45",
  "647fa76f10c98516828586b0": "211.49",
  ...
}
```

### `GET /v1/{id}`

Use request body to query the dataset identified by the given ID, and returns a list of IDs of the entries satisfying
the condition.

Sample interaction

```
curl -i --request GET \
    --header "Content-Type: application/json" \
    --data '{posX: "65.15"}' \
    http://localhost:8080/v1/cern-open-data:13128:237040910_EventInfo
```

```json
[
  "Document{{_id=647fa76f10c98516828586d2}}"
]
```

### `GET /v1/metadata/{id}`

The given `{id}` is used to locate metadata for an imported dataset. If found, the JSON representation of the
appropriate `DatasetMetadata` is returned.

Sample interaction:

```
curl -i --request GET \
    http://localhost:8080/v1/metadata/cern-open-data:13128:237040910_EventInfo
```

```json
{
  "datasetId": "cern-open-data:13128:237040910_EventInfo",
  "fileName": "237040910_EventInfo.csv",
  "type": "CSV",
  "sizeInBytes": 52,
  "numberOfColumns": 3,
  "commaSeparatedColumnNames": "evID,timestamp,muMom",
  "importTimestamp": 1686615824740,
  "collectionMetadata": {
    "id": "cern-open-data:13128",
    "name": "13128",
    "experimentName": "OPERA",
    "eventsCount": 1,
    "type": "Derived",
    "keyword": "",
    "tag": "CERN-SPS",
    "citeText": "Cite as: OPERA collaboration (2019). OPERA neutrino-induced charmed hadron event 237040910. CERN Open Data Portal. ",
    "doi": "10.7483/OPENDATA.OPERA.Q74R.SYBQ",
    "license": "Creative Commons CC0 waiver",
    "creator": "curl/7.76.1",
    "title": "OPERA neutrino-induced charmed hadron event 237040910",
    "publisher": "OPERA",
    "publicationYear": 2019,
    "language": "English",
    "subject": "High Energy Physics, Theoretical Physics",
    "description": "This OPERA detector event is a muon neutrino interaction with the lead target where a charmed hadron was reconstructed in the final state. The event data consist of Electronic Detector files (such as Drift Tube, RPC, and Target Tracker files) and Emulsion Detector files (such as Tracks and Vertex files). For more information, see the description of the whole dataset.",
    "geoLocation": "46.233832398 6.053166454",
    "fundingReference": "https://perma.cc/L34T-TCTG"
  }
}
```

### `GET /v1/metadata`

Return a sorted collection of all metadata objects stored in the database.

Sample interaction:

```
curl -i --request GET \
    http://localhost:8080/v1/metadata
```

If a request body is attached, it will be used to query the collection entries, and the result will contain all the
entries which satisfy the condition.

## TODO

- [x] CRU~~D~~ operations for datasets
    - [x] Idempotent create (update not needed)
    - [x] Thread-safe create endpoint
    - [x] Import CSV datasets
    - [x] Import ROOT datasets
- [ ] ~~Make sure columns have the right typing at import-time~~
- [ ] ~~Endpoints for simple calculations~~ (won't do, data is stored as strings for now)
- [x] Endpoints for simple querying
    - [x] Endpoint to get column names
    - [x] Endpoint to extract column content
    - [x] Endpoint to extract IDs satisfying a condition
- [ ] Data lifecycle
- [x] Docker image
    - [x] Quarkus native image
- [ ] ~~Tests~~
- [x] Document REST endpoints to be more FAIR (`/q/swagger-ui`, parsable version at `q/openapi`)
