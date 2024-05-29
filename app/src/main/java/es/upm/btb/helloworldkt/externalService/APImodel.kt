package es.upm.btb.helloworldkt.externalService

import com.google.gson.annotations.SerializedName


data class APImodel(
    @SerializedName("@graph")
    val model1: List<Model>,

    )

data class Model(
    val uid: String,
    val dtend: String,
    val location: Location,
    val eventLocation: String,
    val link: String,
    val relation: String,
    @SerializedName("@id")
    val id: String,
    val organization: Organization,
    val title: String,
    val dtstart: String,
    val references: String,
    val recurrence: Recurrence,
    val price: Int,
    val address: Address,
    val description: String,
    val excludedDays: String
)




data class Location(
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("latitude")
    val latitude: Double
)

data class Organization(
    val accessibility: String,
    val services: String,
    val schedule: String,
    val organizationName: String,
    @SerializedName("organization-desc")
    val organizationDesc: String
)

data class Recurrence(
    val interval: Int,
    val days: String,
    val frequency: String
)

data class Address(
   // val area: String,
    val locality: String,
    //val district: String,
    @SerializedName("street-address")
    val streetAddress: String,
    @SerializedName("postal-code")
    val postalCode: String
)
