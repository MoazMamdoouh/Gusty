package com.example.gusty.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.gusty.data.model.curren_weather_dto.CurrentWeatherDto

@Entity(tableName = "weather_favorite_items")
data class FavoriteEntity (
    @PrimaryKey(autoGenerate = true)
    val id : Int ,
    val lat : Double ,
    val lon : Double  ,
    val countryName : String ,
    val cityName : String,

)
fun CurrentWeatherDto.convertCurrentDtoToEntity() : FavoriteEntity{
    return FavoriteEntity(
        id = id,
        lat = coord.lat ,
        lon = coord.lon ,
        countryName = sys.country ,
        cityName = name ,
    )
}