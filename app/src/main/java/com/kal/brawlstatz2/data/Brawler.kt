package com.kal.brawlstatz2.data

import android.os.Parcel
import android.os.Parcelable
import androidx.compose.ui.graphics.Color
import com.kal.brawlstatz2.ui.theme.chromatic
import com.kal.brawlstatz2.ui.theme.epic
import com.kal.brawlstatz2.ui.theme.legendary
import com.kal.brawlstatz2.ui.theme.mythic
import com.kal.brawlstatz2.ui.theme.rare
import com.kal.brawlstatz2.ui.theme.starting
import com.kal.brawlstatz2.ui.theme.superrare
import java.io.Serializable

data class Brawler(
    val id: Int?=0,
    val bname: String?=null,
    val brare: String?=null,
    var bpro: String?=null,
    val babout: String?=null,
    var bmodel: String?=null,
    val model3d: String?=null,
    val mastery: String?=null,
    val movementSpeed: String?=null,
    var classType:String?=null,
    var c1: String?=null,
    val c1n: String?=null,
    var c2: String?=null,
    val c2n: String?=null,
    var c3: String?=null,
    val c3n: String?=null,
    var g1: String?=null,
    val g1t: String?=null,
    var g2: String?=null,
    val g2t: String?=null,
    var s1: String?=null,
    val s1t: String?=null,
    var s2: String?=null,
    val s2t: String?=null,
    val battack: String?=null,
    val bsuper: String?=null,
    val battackt: String?=null,
    val bsupert: String?=null,
    var trait: String?=null,
    val tier: String?=null,
    val bstarpower: String?=null,
    val bgadget: String?=null,
    var bgear1: String?=null,
    var bgear2: String?=null,
    var bgear3: String?=null,
    var model : String?=null,
    val zver : String?=null,
    var color : Color=Color.Black,

    ):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        when(parcel.readString()){
            "LEGENDARY" -> legendary
            "MYTHIC" ->mythic
            "CHROMATIC" ->chromatic
            "EPIC" ->epic
            "SUPER RARE" -> superrare
            "RARE" ->rare
            "STARTING" ->starting
            else -> {Color.Black}
        }
    ) {
    }

    constructor() :this(0,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
        Color.Black)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(bname)
        parcel.writeString(brare)
        parcel.writeString(bpro)
        parcel.writeString(babout)
        parcel.writeString(bmodel)
        parcel.writeString(model3d)
        parcel.writeString(mastery)
        parcel.writeString(movementSpeed)
        parcel.writeString(classType)
        parcel.writeString(c1)
        parcel.writeString(c1n)
        parcel.writeString(c2)
        parcel.writeString(c2n)
        parcel.writeString(c3)
        parcel.writeString(c3n)
        parcel.writeString(g1)
        parcel.writeString(g1t)
        parcel.writeString(g2)
        parcel.writeString(g2t)
        parcel.writeString(s1)
        parcel.writeString(s1t)
        parcel.writeString(s2)
        parcel.writeString(s2t)
        parcel.writeString(battack)
        parcel.writeString(bsuper)
        parcel.writeString(battackt)
        parcel.writeString(bsupert)
        parcel.writeString(trait)
        parcel.writeString(tier)
        parcel.writeString(bstarpower)
        parcel.writeString(bgadget)
        parcel.writeString(bgear1)
        parcel.writeString(bgear2)
        parcel.writeString(bgear3)
        parcel.writeString(model)
        parcel.writeString(zver)
        parcel.writeString(brare)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Brawler> {
        override fun createFromParcel(parcel: Parcel): Brawler {
            return Brawler(parcel)
        }

        override fun newArray(size: Int): Array<Brawler?> {
            return arrayOfNulls(size)
        }
    }
}

