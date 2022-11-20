package com.yosef.chapa_in_appurchase

import android.app.Application
import com.yosef.chapainapppurchase.Chapa
import com.yosef.chapainapppurchase.ChapaConfiguration
import com.yosef.chapainapppurchase.enums.Currency
import com.yosef.chapainapppurchase.model.Customization

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // initialize chapa configuration
        val config = ChapaConfiguration()
        config.key = "YOUR-CHAPA-KEY"
        // for security purpose it is better to use decrypted key
        // to get decrypt key use Cipher class,
        // Log.d("Chapa-key",Cipher.decrypt(this,"YOUR CHAPA KEY"))
        // config.key = Cipher.decrypt(this, "DECRYPTED_CHAPA-KEY")
        config.currency = Currency.ETB
        config.showPaymentError(true)
        config.callbackUrl = "https://example.com/api/callback"
//        config.customer = Customer("first_name","last_name","example@mail.com")
        config.customization = Customization("title", "description", "logo-url")
        Chapa.init(applicationContext, config)
    }
}