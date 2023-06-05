# Data model

## Conceptual data model

- `DatasetMetadata`: Important information about the dataset (i.e. no numerical things which go to the database)
- `Dataset`: The actual dataset with numerical data split in named columns

### Scaling `Dataset` horizontally

Since each dataset has its own set of column names, each dataset is stored in a separate table.

## Logical data model

### `DatasetMetadata`

- ID
- Creation time
- Author
- Experiment
- Source
- Original type (ROOT, CSV, ...)

### `Dataset`

- ID
- Columns with numerical data

## Physical data model

### Keys

ID is used as a key to find metadata. The table name for the dataset is the ID.

### Constraints

All constraints will be on the metadata fields. For instance, original type allowed is constrained to a set of defined
types.

### Other implementation requirements

We require that at a given point in time no database exists without an associated metadata, and viceversa.