package com.yosef.chapa_in_appurchase.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.yosef.chapa_in_appurchase.R
import com.yosef.chapa_in_appurchase.data.StoreItem
import com.yosef.chapainapppurchase.Chapa
import com.yosef.chapainapppurchase.ChapaError
import com.yosef.chapainapppurchase.PurchasedItems
import com.yosef.chapainapppurchase.enums.ItemProperties
import com.yosef.chapainapppurchase.interfaces.ChapaPaymentCallback
import com.yosef.chapainapppurchase.payment_type.ItemPayment
import kotlin.reflect.KFunction1

class StoreListAdapter(private val context: Context, private val list: ArrayList<StoreItem>) :
    RecyclerView.Adapter<StoreListAdapter.ViewHolder>() {
    var chapa: Chapa = Chapa.getInstance()
    val purchasedItems = PurchasedItems(context)

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val amount: Button
        val item: TextView
        val value: TextView

        init {
            amount = view.findViewById(R.id.item_amount)
            item = view.findViewById(R.id.item_title)
            value = view.findViewById(R.id.item_value)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.store_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        // check if the item is already purchased
        val isPurchased =
            item.data.itemProperties == ItemProperties.REPLACE && purchasedItems.isPurchased(
                item.data.itemKey, item.data.value
            )
        if (!isPurchased) holder.amount.text = "${item.data.amount} 💰"
        else holder.amount.text = "Use"
        holder.item.text = item.title
        if (item.data.itemProperties != ItemProperties.REPLACE) holder.value.text =
            item.data.value.toString()


        holder.amount.setOnClickListener {
            if (item.data.itemProperties == ItemProperties.REPLACE && isPurchased)
                item.onSuccess(item.data.itemKey)
            else
                processItemPayment(position, item.data, item.onSuccess)
        }
    }

    private fun processItemPayment(
        position: Int,
        item: ItemPayment, successCallback: KFunction1<String, Unit>
    ) {
        chapa.pay(context, item, object : ChapaPaymentCallback<ItemPayment> {
            /**
             * Called when payment is successful
             *
             * @param paymentType PaymentType object
             * @param tx_ref      Transaction reference of the payment
             */
            override fun onSuccess(tx_ref: String, paymentType: ItemPayment) {
                Toast.makeText(
                    context,
                    "Payment Successful!\n${paymentType.itemKey}:${paymentType.updatedValue}",
                    Toast.LENGTH_SHORT
                ).show()
                successCallback(paymentType.itemKey)
                this@StoreListAdapter.notifyItemChanged(position)
            }

            /**
             * Called when error occurred in payment
             */
            override fun onError(chapaError: ChapaError) {
                Toast.makeText(
                    context, "Payment Failed!\n${chapaError.message}", Toast.LENGTH_SHORT
                ).show()
            }

            /**
             * Called when payment is canceled
             */
            override fun onCancel() {
                Toast.makeText(context, "Payment Canceled!", Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
