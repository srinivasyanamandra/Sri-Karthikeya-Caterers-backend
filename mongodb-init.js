// MongoDB Initialization Script
// Run this script after starting MongoDB to set up the database

use sri-karthikeya-caterers

// Create collections
db.createCollection("menu")
db.createCollection("gallery")
db.createCollection("reviews")
db.createCollection("quotes")

// Create indexes for Menu
db.menu.createIndex({ "imageId": 1 }, { unique: true, name: "idx_menu_imageId" })
db.menu.createIndex({ "name": 1 }, { name: "idx_menu_name" })
db.menu.createIndex({ "price": 1 }, { name: "idx_menu_price" })
db.menu.createIndex({ "createdAt": -1 }, { name: "idx_menu_createdAt" })

// Create indexes for Gallery
db.gallery.createIndex({ "imageId": 1 }, { unique: true, name: "idx_gallery_imageId" })
db.gallery.createIndex({ "type": 1 }, { name: "idx_gallery_type" })
db.gallery.createIndex({ "name": 1 }, { name: "idx_gallery_name" })
db.gallery.createIndex({ "createdAt": -1 }, { name: "idx_gallery_createdAt" })

// Create indexes for Reviews
db.reviews.createIndex({ "imageId": 1 }, { unique: true, name: "idx_reviews_imageId" })
db.reviews.createIndex({ "stars": -1 }, { name: "idx_reviews_stars" })
db.reviews.createIndex({ "type": 1 }, { name: "idx_reviews_type" })
db.reviews.createIndex({ "guestsCount": -1 }, { name: "idx_reviews_guestsCount" })
db.reviews.createIndex({ "createdAt": -1 }, { name: "idx_reviews_createdAt" })

// Create indexes for Quotes
db.quotes.createIndex({ "email": 1 }, { name: "idx_quotes_email" })
db.quotes.createIndex({ "phoneNumber": 1 }, { name: "idx_quotes_phoneNumber" })
db.quotes.createIndex({ "eventDate": 1 }, { name: "idx_quotes_eventDate" })
db.quotes.createIndex({ "eventType": 1 }, { name: "idx_quotes_eventType" })
db.quotes.createIndex({ "createdAt": -1 }, { name: "idx_quotes_createdAt" })

// Insert sample data for testing (optional)

// Sample Menu
db.menu.insertOne({
    "_id": "550e8400-e29b-41d4-a716-446655440000",
    "imageId": "550e8400-e29b-41d4-a716-446655440001",
    "name": "Wedding Special Package",
    "price": 599.99,
    "description": "Complete wedding catering package with traditional dishes including biryani, curries, and desserts",
    "items": [
        "Chicken Biryani",
        "Paneer Butter Masala",
        "Dal Makhani",
        "Butter Naan",
        "Raita",
        "Gulab Jamun",
        "Ice Cream"
    ],
    "createdAt": new Date(),
    "updatedAt": new Date()
})

// Sample Gallery
db.gallery.insertOne({
    "_id": "660e8400-e29b-41d4-a716-446655440000",
    "imageId": "660e8400-e29b-41d4-a716-446655440001",
    "type": "SERVICES",
    "name": "Professional Catering Service",
    "description": "Our experienced team providing excellent catering services for your special events",
    "createdAt": new Date(),
    "updatedAt": new Date()
})

// Sample Review
db.reviews.insertOne({
    "_id": "770e8400-e29b-41d4-a716-446655440000",
    "imageId": "770e8400-e29b-41d4-a716-446655440001",
    "timeline": "December 2023",
    "guestsCount": 500,
    "stars": 5,
    "comments": "Excellent service and delicious food. The team was very professional and handled everything perfectly. Highly recommended for weddings!",
    "topPicks": ["FOOD", "SERVICE", "PRESENTATION"],
    "type": "WEDDING",
    "createdAt": new Date(),
    "updatedAt": new Date()
})

// Sample Quote
db.quotes.insertOne({
    "_id": "880e8400-e29b-41d4-a716-446655440000",
    "fullName": "John Doe",
    "phoneNumber": "+919876543210",
    "email": "john.doe@example.com",
    "eventDate": new Date("2024-12-25"),
    "eventType": "WEDDING",
    "expectedGuests": 300,
    "additionalDetails": "Need vegetarian options only. Event will be held outdoors.",
    "createdAt": new Date(),
    "updatedAt": new Date()
})

// Verify collections and indexes
print("\n=== Collections ===")
db.getCollectionNames().forEach(function(collection) {
    print(collection)
})

print("\n=== Menu Indexes ===")
db.menu.getIndexes().forEach(function(index) {
    print(index.name)
})

print("\n=== Gallery Indexes ===")
db.gallery.getIndexes().forEach(function(index) {
    print(index.name)
})

print("\n=== Reviews Indexes ===")
db.reviews.getIndexes().forEach(function(index) {
    print(index.name)
})

print("\n=== Quotes Indexes ===")
db.quotes.getIndexes().forEach(function(index) {
    print(index.name)
})

print("\n=== Document Counts ===")
print("Menu: " + db.menu.countDocuments())
print("Gallery: " + db.gallery.countDocuments())
print("Reviews: " + db.reviews.countDocuments())
print("Quotes: " + db.quotes.countDocuments())

print("\nDatabase initialization completed successfully!")
