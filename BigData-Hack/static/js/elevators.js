function requestElevatorUpdate(layerGroup, map) {
    var elevatorIcon = L.icon({
      iconUrl: '../static/img/Elevator.png',
      iconSize: [30, 30],
      iconAnchor: [10, 0]
    });
  function drawElevatorData(data) {
    var marker; 
    // TODO: turn this into map tiles for several zoom levels to speed
    // things up (slowness is due to drawing so many lines)
    layerGroup.clearLayers();
    var bounds = map.getBounds();
    for (i = 0; i < data.length; i++) {
      var geoJSON = data[i];
      var coord = geoJSON['geometry']['coordinates'];
      var description = geoJSON['properties']['description']; 
      var latlng = [coord[1], coord[0]];
      if (bounds.contains(latlng)) {
          marker = L.geoJson(geoJSON, {
            pointToLayer: function(feature, latlng) {
              return L.marker(latlng, {icon: elevatorIcon});
            }
          });

          //Display info when user clicks on the curb marker
          var popup = L.popup().setContent("<b>Elevator</b><br><p>" + description + "</p>");
          marker.bindPopup(popup);

        layerGroup.addLayer(marker);
      }
    }
  }

$.when(elevatorDataRequest).done(function(data) {
  drawElevatorData(data);
});
}
