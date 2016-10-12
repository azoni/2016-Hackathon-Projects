function requestUserDataUpdate(layerGroup, map) {
    
    var constructionIcon = L.icon({
      iconUrl: '../static/img/construction.png',
      iconSize: [30, 30],
      iconAnchor: [10, 0]
    });
    var stairsIcon = L.icon({
      iconUrl: '../static/img/stairs.png',
      iconSize: [30, 30],
      iconAnchor: [10, 0]
    });
    var elevatorIcon = L.icon({
      iconUrl: '../static/img/Elevator.png',
      iconSize: [30, 30],
      iconAnchor: [10, 0]
    });
    var otherIcon = L.icon({
      iconUrl: '../static/img/others.png',
      iconSize: [30, 30],
      iconAnchor: [10, 0]
    });

  function drawUserData(data) {
    
    var marker; 
    // TODO: turn this into map tiles for several zoom levels to speed
    // things up (slowness is due to drawing so many lines)
    layerGroup.clearLayers();
    var bounds = map.getBounds();
    for (i = 0; i < data.length; i++) {
      var geoJSON = data[i];
      var coord = geoJSON['geometry']['coordinates'];
      var type = geoJSON['properties']['type']; 
      var latlng = [coord[1], coord[0]];
      if (bounds.contains(latlng)) {
        if(type === "Construction") {
          marker = L.geoJson(geoJSON, {
            pointToLayer: function(feature, latlng) {
              console.log("here"); 
              return L.marker(latlng, {icon: constructionIcon});
            }
          });


          //Display info when user clicks on the curb marker
          var popup = L.popup().setContent("<b>Construction</b>");
          marker.bindPopup(popup);
        }
        else if(type === "Stairs") {
          marker = L.geoJson(geoJSON, {
            pointToLayer: function(feature, latlng) {
              console.log("here"); 
              return L.marker(latlng, {icon: stairsIcon});
            }
          });


          //Display info when user clicks on the curb marker
          var popup = L.popup().setContent("<b>Stairs</b>");
          marker.bindPopup(popup);
        }
        else if(type === "Elevator") {
          marker = L.geoJson(geoJSON, {
            pointToLayer: function(feature, latlng) {
              console.log("here"); 
              return L.marker(latlng, {icon: elevatorIcon});
            }
          });


          //Display info when user clicks on the curb marker
          var popup = L.popup().setContent("<b>Elevator</b>");
          marker.bindPopup(popup);
        }
        else if(type === "Other") {
          marker = L.geoJson(geoJSON, {
            pointToLayer: function(feature, latlng) {
              console.log("here"); 
              return L.marker(latlng, {icon: otherIcon});
            }
          });


          //Display info when user clicks on the curb marker
          var popup = L.popup().setContent("<b>Other Obstacle</b>");
          marker.bindPopup(popup);
        }
        else if(type === "Curb Ramp") {
          marker = L.geoJson(geoJSON, {
            pointToLayer: function(feature, latlng) {
              return L.circleMarker(latlng, {
                'radius': 3,
                'color': '#0000FF'
              })
            }
          });


          //Display info when user clicks on the curb marker
          var popup = L.popup().setContent("<b>Curb Ramp</b>");
          marker.bindPopup(popup);
        }


        layerGroup.addLayer(marker);
      }
    }
  }

$.when(userDataDataRequest).done(function(data) {
  drawUserData(data);
});
}
