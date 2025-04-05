package com.example.gusty.data.local.favorite

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.gusty.data.model.curren_weather_dto.CurrentWeatherDto
import java.util.Locale

@Entity(tableName = "weather_favorite_items")
data class FavoriteEntity (
    @PrimaryKey(autoGenerate = true)
    val id : Int ,
    val lat : Double ,
    val lon : Double  ,
    val countryName : String ,
    val cityName : String,

)
fun CurrentWeatherDto.convertCurrentDtoToEntity() : FavoriteEntity {
    val countryName = getCountryNameFromCode(sys.country)
    return FavoriteEntity(
        id = id,
        lat = coord.lat ,
        lon = coord.lon ,
        countryName = countryName ,
        cityName = name ,
    )
}

fun getCountryNameFromCode(countryCode: String): String {
    val locale = Locale("", countryCode)
    return locale.displayCountry
}