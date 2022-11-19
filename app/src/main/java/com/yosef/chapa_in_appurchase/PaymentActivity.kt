package com.yosef.chapa_in_appurchase

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.yosef.chapa_in_appurchase.databinding.ActivityPaymentBinding
import com.yosef.chapainapppurchase.Chapa
import com.yosef.chapainapppurchase.ChapaError
import com.yosef.chapainapppurchase.interfaces.ChapaPaymentCallback
import com.yosef.chapainapppurchase.payment_type.AppPayment

class PaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentBinding
    private lateinit var chapa: Chapa

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val standardBtn = binding.buyStandard

        chapa = Chapa.getInstance()
        // setup App Payment - [Premium feature]
        val standardPlan = AppPayment(this, 9.99, "Standard", MainActivity::class.java)

        standardBtn.setOnClickListener {
            chapa.pay(this, standardPlan, object : ChapaPaymentCallback<AppPayment> {
                /**
                 * Called when payment is successful
                 *
                 * @param paymentType PaymentType object
                 * @param tx_ref      Transaction reference of the payment
                 */
                override fun onSuccess(tx_ref: String, paymentType: AppPayment) {
                    Toast.makeText(
                        this@PaymentActivity, "Payment Success, tx-ref : $tx_ref", Toast.LENGTH_LONG
                    ).show()
                }

                /**
                 * Called when error occurred in payment
                 */
                override fun onError(chapaError: ChapaError) {
                    Toast.makeText(
                        this@PaymentActivity,
                        "Payment Fail  : ${chapaError.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }

                /**
                 * Called when payment is canceled
                 */
                override fun onCancel() {
                    Toast.makeText(this@PaymentActivity, "Payment Canceled", Toast.LENGTH_LONG)
                        .show()
                }

            })
        }


    }
}
