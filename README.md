# Chapa-In-App-Purchase
Unoffical Android In-App Purchase Library using Chapa

[View Documentation](https://yosefw1221.github.io/chapa-in-app-purchase-doc/)

[Download Sample App](https://bit.ly/example-apk)

# Getting Started

## Installation

> **minSdk : 19**

**Step 1 :** Open ```setting.gradle``` file add maven jetpack repository:

```gradle
dependencyResolutionManagement {
repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
repositories {
    google()
    mavenCentral()
    maven { url 'https://jitpack.io' }
 }
}
```

**Step 2 :** On your ```build.gradle``` file add the following dependency:

```gradle
dependencies {
    implementation 'com.github.yosefw1221:Chapa-In-App-Purchase:1.0.0-beta'
    ...
}
```

## Initialize Configuration

**Step 3 :** Initialize chapa configuration on your app's ```MainActivity``` or ```Application``` class

```kotlin
val config = ChapaConfiguration()
config.key = "YOUR-CHAPA-SECRET-KEY" // (Required)
// for security purpose it is better to use encrypt key
// to get encrypted key use Cipher class,
// Log.d("Chapa-key",Cipher.encrypt(this,"YOUR CHAPA-SECRET-KEY"))
// config.key = Cipher.decrypt(this, "DECRYPTED_CHAPA-SECRET-KEY")
config.currency = Currency.ETB // Currency.USD --Default ETB

// config.callbackUrl = "https://example.com/api/callback"  (Optional)

// config.customer = Customer("first_name","last_name","example@mail.com") (Optional)  Dialog will show to customer, to fill their infomation
// config.customization = Customization("title","description","logo-url")

Chapa.init(applicationContext, config)

```

## Basic Usage

**Step 4 :** To process simple basic payment

```kotlin
// get chapa instance
val chapa = Chapa.getInstance()
// setup basic payment type
val basic = BasicPayment( /*amount*/ 9.99)

chapa.pay(/* activityContext */ this, basic, object : ChapaPaymentCallback<BasicPayment> {

 /**
  * Called when payment is successful
  *
  * @param paymentType PaymentType object
  * @param tx_ref      Transaction reference of the payment
  */
 override fun onSuccess(tx_ref: String, paymentType: BasicPayment) {
  // TODO your code here
 }

 /**
  * Called when error occurred in payment
  */
 override fun onError(chapaError: ChapaError) {
  // TODO your code here
 }

 /**
  * Called when payment is canceled
  */
 override fun onCancel() {
  // TODO your code here
 }

})
```
