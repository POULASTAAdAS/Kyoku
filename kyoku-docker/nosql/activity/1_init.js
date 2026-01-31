db = db.getSiblingDB('activity_db');

db.createCollection('userGenre', {
    validator: {
        $jsonSchema: {
            bsonType: "object",
            required: ["userId", "genreIds"],
            properties: {
                userId: {
                    bsonType: "long",
                    description: "User ID must be a long and is required"
                },
                genreIds: {
                    bsonType: "array",
                    items: {
                        bsonType: "int"
                    },
                    description: "Array of genre IDs (integers) and is required"
                },
                createdAt: {
                    bsonType: "date",
                    description: "Timestamp when the record was created"
                },
                updatedAt: {
                    bsonType: "date",
                    description: "Timestamp when the record was last updated"
                }
            }
        }
    }
});

db.createCollection('userArtist', {
    validator: {
        $jsonSchema: {
            bsonType: "object",
            required: ["userId", "artistIds"],
            properties: {
                userId: {
                    bsonType: "long",
                    description: "User ID must be a long and is required"
                },
                artistIds: {
                    bsonType: "array",
                    items: {
                        bsonType: "long"
                    },
                    description: "Array of genre IDs (integers) and is required"
                },
                createdAt: {
                    bsonType: "date",
                    description: "Timestamp when the record was created"
                },
                updatedAt: {
                    bsonType: "date",
                    description: "Timestamp when the record was last updated"
                }
            }
        }
    }
});




db.userGenre.createIndex({ "userId": 1 }, { unique: true });
db.userArtist.createIndex({ "userId": 1 }, { unique: true });

print('Database initialized successfully');