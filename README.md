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
- JSoup
- Slf4j + Logback
- Swagger (OpenAPI)

### Build tools & plugins

- Quarkus CLI
- Gradle (Kotlin DSL)
- Spotless
- Lombok

## Model objects

The following models are defined in the package `io.github.fandreuz.root.data.server.models`:

### `CollectionMetadata`

- ID
- Name
- Short description
- Long description
- Year
- Experiment name
- Events count
- Type
- Keyword
- Tag
- Cite text
- DOI
- License

**Example**:

```json
{
  "id": "cern-open-data:13128",
  "name": "13128",
  "shortDescription": "OPERA neutrino-induced charmed hadron event 237040910",
  "longDescription": "This OPERA detector event is a muon neutrino interaction with the lead target where a charmed hadron was reconstructed in the final state. The event data consist of Electronic Detector files (such as Drift Tube, RPC, and Target Tracker files) and Emulsion Detector files (such as Tracks and Vertex files). For more information, see the description of the whole dataset.",
  "year": 2019,
  "experimentName": "OPERA",
  "eventsCount": 1,
  "type": "Derived",
  "keyword": "",
  "tag": "CERN-SPS",
  "citeText": "Cite as: OPERA collaboration (2019). OPERA neutrino-induced charmed hadron event 237040910. CERN Open Data Portal. ",
  "doi": "10.7483/OPENDATA.OPERA.Q74R.SYBQ",
  "license": "Creative Commons CC0 waiver"
}
```

### `DatasetMetadata`

- ID
- File name
- Dataset type (ROOT, CSV, JSON, ...)
- Size in bytes
- Number of columns
- Comma separated column names
- Import timestamp
- Collection metadata

**Example**:

```json
{
  "id": "cern-open-data:13128:1686475345237",
  "fileName": "1686475345237.csv",
  "type": "CSV",
  "sizeInBytes": 735,
  "numberOfColumns": 3,
  "commaSeparatedColumnNames": "posX,posZ,driftDist",
  "importTimestamp": 1686475345479,
  "collectionMetadata": {
    "id": "cern-open-data:13128",
    "name": "13128",
    "shortDescription": "OPERA neutrino-induced charmed hadron event 237040910",
    "longDescription": "This OPERA detector event is a muon neutrino interaction with the lead target where a charmed hadron was reconstructed in the final state. The event data consist of Electronic Detector files (such as Drift Tube, RPC, and Target Tracker files) and Emulsion Detector files (such as Tracks and Vertex files). For more information, see the description of the whole dataset.",
    "year": 2019,
    "experimentName": "OPERA",
    "eventsCount": 1,
    "type": "Derived",
    "keyword": "",
    "tag": "CERN-SPS",
    "citeText": "Cite as: OPERA collaboration (2019). OPERA neutrino-induced charmed hadron event 237040910. CERN Open Data Portal. ",
    "doi": "10.7483/OPENDATA.OPERA.Q74R.SYBQ",
    "license": "Creative Commons CC0 waiver"
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
curl -i --request PUT \
    --header "Content-Type: application/json" \
    --data '{"collectionId":"13128","fileName":"237040910_FilteredDTHitsXZ.csv"} \
    http://localhost:8080/v1'
```

```json
{
  "id": "13128-237040910_FilteredDTHitsXZ.csv",
  "collectionName": "13128",
  "fileName": "237040910_FilteredDTHitsXZ.csv",
  "type": "CSV",
  "importTimestamp": 1686087535325
}
```

### `GET /v1/{id}/column-names`

Get the name of the columns for the given dataset.

Sample interaction:

```
curl -i --request GET \ 
    http://localhost:8080/v1/13128-237040910_FilteredDTHitsXZ.csv/column-names
```

```
["_id","driftDist","posX","posZ"]
```

### `GET /v1/{id}/{column-name}`

Get the content of a column for the given dataset.

Sample interaction:

```
curl -i --request GET \  
    http://localhost:8080/v1/13128-237040910_FilteredDTHitsXZ.csv/posX
```

```json
{
  "647fa76f10c98516828586ae": "66.55",
  "647fa76f10c98516828586af": "64.45",
  "647fa76f10c98516828586b0": "211.49",
  "647fa76f10c98516828586b1": "67.55",
  "647fa76f10c98516828586b2": "65.45",
  "647fa76f10c98516828586b3": "65.46",
  "647fa76f10c98516828586b4": "67.57",
  "647fa76f10c98516828586b5": "66.47",
  "647fa76f10c98516828586b6": "64.37",
  "647fa76f10c98516828586b7": "60.94",
  "647fa76f10c98516828586b8": "65.14",
  "647fa76f10c98516828586b9": "67.24",
  "647fa76f10c98516828586ba": "-352.9",
  "647fa76f10c98516828586bb": "66.15",
  "647fa76f10c98516828586bc": "68.25",
  "647fa76f10c98516828586bd": "66.59",
  "647fa76f10c98516828586be": "68.69",
  "647fa76f10c98516828586bf": "67.59",
  "647fa76f10c98516828586c0": "69.69",
  "647fa76f10c98516828586c1": "52.89",
  "647fa76f10c98516828586c2": "57.09",
  "647fa76f10c98516828586c3": "69.69",
  "647fa76f10c98516828586c4": "50.79",
  "647fa76f10c98516828586c5": "53.89",
  "647fa76f10c98516828586c6": "62.29",
  "647fa76f10c98516828586c7": "70.69",
  "647fa76f10c98516828586c8": "55.99",
  "647fa76f10c98516828586c9": "60.19",
  "647fa76f10c98516828586ca": "64.39",
  "647fa76f10c98516828586cb": "68.59",
  "647fa76f10c98516828586cc": "72.79",
  "647fa76f10c98516828586cd": "-275.81",
  "647fa76f10c98516828586ce": "-130.1",
  "647fa76f10c98516828586cf": "-125.9",
  "647fa76f10c98516828586d0": "-121.7",
  "647fa76f10c98516828586d1": "71.45",
  "647fa76f10c98516828586d2": "65.15",
  "647fa76f10c98516828586d3": "69.35",
  "647fa76f10c98516828586d4": "70.35"
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
    http://localhost:8080/v1/13128-237040910_FilteredDTHitsXZ.csv
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
    http://localhost:8080/v1/metadata/13128-237040910_FilteredDTHitsXZ.csv
```

```json
{
  "id": "13128-237040910_FilteredDTHitsXZ.csv",
  "collectionName": "13128",
  "fileName": "237040910_FilteredDTHitsXZ.csv",
  "type": "CSV",
  "importTimestamp": 1686087535325
}
```

### `GET /v1/metadata`

Return a sorted collection of all metadata objects stored in the database.

```
curl -i --request GET \ 
    http://localhost:8080/v1/metadata/
```

```json
[
  {
    "id": "13128-237040910_FilteredDTHitsXZ.csv",
    "collectionName": "13128",
    "fileName": "237040910_FilteredDTHitsXZ.csv",
    "type": "CSV",
    "importTimestamp": 1686087535325
  }
]
```

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
- [ ] Docker image
    - [ ] Quarkus native image
    - [ ] MongoDB
- [ ] ~~Tests~~
- [x] Document REST endpoints to be more FAIR (`/q/swagger-ui`, parsable version at `q/openapi`)
