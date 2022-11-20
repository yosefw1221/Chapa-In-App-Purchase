package com.yosef.chapa_in_appurchase

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yosef.chapa_in_appurchase.adapter.StoreListAdapter
import com.yosef.chapa_in_appurchase.data.StoreItem
import com.yosef.chapa_in_appurchase.databinding.ActivityMainBinding
import com.yosef.chapainapppurchase.Chapa
import com.yosef.chapainapppurchase.ChapaUtil
import com.yosef.chapainapppurchase.PurchasedItems
import com.yosef.chapainapppurchase.enums.Currency
import com.yosef.chapainapppurchase.enums.ItemProperties
import com.yosef.chapainapppurchase.payment_type.AppPayment
import com.yosef.chapainapppurchase.payment_type.ItemPayment
import com.yosef.chapainapppurchase.utils.Cipher

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var emoji: TextView
    private lateinit var upgradeBtn: Button
    private lateinit var upgradeCard: CardView
    private lateinit var adsSample: LinearLayout
    private lateinit var disableAds: Button
    private lateinit var coin: TextView
    private lateinit var gems: TextView
    private lateinit var storeList: RecyclerView
    private lateinit var chapa: Chapa
    private lateinit var purchasedItems: PurchasedItems

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        chapa = Chapa.getInstance()
        purchasedItems = PurchasedItems(this)
        val plan = binding.title
        adsSample = binding.adsSample
        disableAds = binding.ads.disableAds
        emoji = binding.emoj
        coin = binding.coin
        gems = binding.gems
        upgradeBtn = binding.upgrade
        upgradeCard = binding.upgradeCard
        storeList = binding.storeList
        displayPayedItemValue("")

        // hide upgrade card and ads for premium user
        val isPremiumUser = ChapaUtil.isCurrentPlanIn("Premium")
        if (isPremiumUser) {
            upgradeCard.visibility = CardView.GONE
            adsSample.visibility = LinearLayout.GONE
        }
        Log.e("Backup ", purchasedItems.allEncryptedPayedItemAsJSONString)

        upgradeBtn.setOnClickListener {
            upgradeToPremium()
        }

        disableAds.setOnClickListener {
            AlertDialog.Builder(this).setTitle("Upgrade to Premium")
                .setMessage("Upgrade to Premium to disable ads")
                .setPositiveButton("Upgrade") { _, _ ->
                    upgradeToPremium()
                }.setNegativeButton("Close", null).show()
        }
        // store items
        val store = ArrayList<StoreItem>()
        store.add(
            StoreItem(
                "🪙",
                ItemPayment(this, 1.99, "coin", 10, ItemProperties.ADD),
                ::displayPayedItemValue
            )
        )
        // to secure your item payment use encrypt  'amount' 'itemValue' and 'itemKey'
        Log.i(
            "Encrypted, Key : ",
            Cipher.encrypt(this, "coin") + "\nValue : " + Cipher.encrypt(
                this,
                "25"
            ) + "\nAmount : " + Cipher.encrypt(this, "2.99")
        )
        store.add(
            StoreItem(
                "🪙",
                ItemPayment(
                    this,
                    Cipher.decrypt(this, "C5F373E141957172C4F130A65BFE70B9").toDouble(), // amount
                    Cipher.decrypt(this, "DAABD31A834EEC8E3EE29C819AFD6091"), // itemKey
                    Cipher.decrypt(this, "E6D33CB6CBFB48212745A53469AC5CC0").toInt(), // itemValue
                    ItemProperties.ADD
                ),
                ::displayPayedItemValue
            )
        )
        store.add(
            StoreItem(
                "💎",
                ItemPayment(this, 1.99, "diamond", 10, ItemProperties.ADD),
                ::displayPayedItemValue
            )
        )
        store.add(
            StoreItem(
                "💎",
                ItemPayment(this, 2.99, "diamond", 25, ItemProperties.ADD),
                ::displayPayedItemValue
            )
        )
        store.add(
            StoreItem(
                "🕶", ItemPayment(this, 199.0, "eyeglass", true), ::useItem
            )
        )
        store.add(StoreItem("💍", ItemPayment(this, 999.0, "ring", true), ::useItem))
        store.add(StoreItem("🍺", ItemPayment(this, 1.0, "alcohol", true), ::useItem))
        store.add(
            StoreItem(
                "🧢", ItemPayment(this, 14.99, "snow", true), ::useItem
            )
        )

        val adapter = StoreListAdapter(this, store)
        val grid = GridLayoutManager(this, 4)
        storeList.layoutManager = grid
        storeList.adapter = adapter
        plan.text = "Current Plan : ${Chapa.getCurrentUserAppPlan()}"

    }

    @SuppressLint("SetTextI18n")
    private fun displayPayedItemValue(_v: String) {
        coin.text = "🪙 : ${purchasedItems.getInt("coin")}"
        gems.text = "💎 : ${purchasedItems.getInt("diamond")}"
    }

    private fun upgradeToPremium() {
        val premiumPlan = AppPayment(this, 1.99, "Premium", MainActivity::class.java)
        premiumPlan.currency = Currency.USD
        chapa.pay(this, premiumPlan)
    }

    private fun useItem(item: String) {
        when (item) {
            "eyeglass" -> emoji.text = "😎"
            "ring" -> emoji.text = "😳"
            "alcohol" -> emoji.text = "🥴"
            "snow" -> emoji.text = "🤠"

        }
    }
}