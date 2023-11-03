package ie.setu.food.views.gallery

import ie.setu.food.main.MainApp

class GalleryPresenter(val view: GalleryView) {

    var app: MainApp = view.application as MainApp

    fun getImages() = app.foods.findAll()

}