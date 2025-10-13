# SME POS System - Android Mobile Application

A simple yet standard Point of Sale (POS) system designed for Small and Medium Enterprises (SME). This Android application provides essential features for retail businesses including sales transactions, inventory management, user management, and basic reporting.

## Features

### 📊 Dashboard
- Overview of daily sales and statistics
- Quick navigation to all major sections
- Real-time summary of transactions and products

### 🛍️ Sales Management
- Intuitive product selection interface
- Shopping cart functionality with quantity adjustments
- Multiple payment methods (Cash, Card, Digital)
- Real-time total calculation
- Transaction processing with change calculation

### 📦 Product Management
- Add, edit, and delete products
- Inventory tracking with low stock indicators
- Product categorization
- Search and filter functionality
- Stock quantity management

### 👥 User Management
- Basic user authentication system
- Role-based access (Admin/Cashier)
- User profile management
- Active/Inactive user status

### 📈 Reports & Analytics
- Daily sales summaries
- Weekly performance reports
- Transaction history
- Sales statistics and trends

## Technical Specifications

### Architecture
- **Database**: Room Database (SQLite)
- **UI Pattern**: Activity-based with RecyclerView adapters
- **Threading**: ExecutorService for background operations
- **Design**: Material Design components

### Database Schema
- **Users**: Authentication and role management
- **Products**: Inventory and product information
- **Sales**: Transaction records
- **SaleItems**: Individual items within transactions

### Key Technologies
- Android SDK (API Level 24+)
- Room Database Library
- Material Design Components
- RecyclerView for lists
- CardView for modern UI
- AsyncTask alternatives with ExecutorService

## Installation & Setup

### Prerequisites
- Android Studio
- Android SDK (API 24 or higher)
- Java 11 or higher

### Setup Steps
1. Clone or download the project
2. Open in Android Studio
3. Sync Gradle files
4. Build and run the application

### Default Login Credentials
The app initializes with sample data including:
- **Admin**: username: `admin`, password: `admin123`
- **Cashier**: username: `cashier`, password: `cash123`

## Project Structure

```
app/src/main/java/com/example/pos_project/
├── activity/           # Activity classes
│   ├── MainActivity.java
│   ├── ProductActivity.java
│   ├── AddEditProductActivity.java
│   ├── SalesActivity.java
│   ├── UsersActivity.java
│   └── ReportsActivity.java
├── adapter/           # RecyclerView adapters
│   ├── ProductAdapter.java
│   ├── SaleProductAdapter.java
│   └── CartAdapter.java
├── dao/              # Data Access Objects
│   ├── UserDao.java
│   ├── ProductDao.java
│   ├── SaleDao.java
│   └── SaleItemDao.java
├── database/         # Database setup
│   └── POSDatabase.java
├── model/           # Entity classes
│   ├── User.java
│   ├── Product.java
│   ├── Sale.java
│   ├── SaleItem.java
│   └── CartItem.java
└── utils/           # Utility classes
    └── DatabaseInitializer.java
```

## Sample Data

The application comes pre-loaded with:
- **10 sample products** across different categories (Beverages, Bakery, Dairy, Fruits, etc.)
- **2 user accounts** (Admin and Cashier roles)
- **Product categories** for better organization

## Key Features Implementation

### Sales Process
1. Select products from the product list
2. Add items to cart with quantity adjustments
3. Enter customer information (optional)
4. Select payment method
5. Enter amount paid
6. Process transaction with automatic change calculation
7. Update inventory quantities

### Inventory Management
- Real-time stock tracking
- Low stock warnings (color-coded indicators)
- Automatic quantity reduction on sales
- Easy product addition and editing

### Data Persistence
- All data stored locally using Room Database
- Automatic database initialization
- Transaction safety with proper threading

## Future Enhancements

### Potential Improvements
- Barcode scanning functionality
- Receipt printing integration
- Cloud backup and sync
- Advanced reporting with charts
- Multi-store support
- Tax calculation
- Discount management
- Customer loyalty programs

### Technical Improvements
- ViewBinding implementation
- MVVM architecture with LiveData
- Dependency injection with Hilt
- API integration for cloud sync
- Unit and integration testing

## Usage Guidelines

### For Students
This project demonstrates:
- Android development best practices
- Database design and implementation
- UI/UX design principles
- Business logic implementation
- Error handling and user feedback

### For SME Businesses
- Suitable for small retail operations
- Easy to use interface
- Essential POS functionality
- Local data storage for reliability
- Customizable for specific business needs

## License

This project is created for educational purposes and SME use. Feel free to modify and adapt according to your requirements.

## Support

For questions or issues, please refer to the code documentation and comments throughout the project files.

---

**Note**: This is a basic implementation suitable for learning and small business use. For production environments, consider additional security measures, data backup strategies, and thorough testing.