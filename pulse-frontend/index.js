
var map;
function initMap() {
  map = new google.maps.Map(document.getElementById('map'), {
    center: {lat: -34.9287, lng: 138.5999},
    zoom: 13,
    disableDefaultUI: true,
    scaleControl: true,
    zoomControl: true,
  });

  var icons = {
    wine: {
      icon: 'icons/glass.svg'
    },
    icecream1: {
      icon: 'icons/ice-cream1.svg'
    },
    icecream2: {
      icon: 'icons/ice-cream2.svg'
    },
    icecream3: {
      icon: 'icons/ice-cream3.svg'
    }
  };

  function addMarker(feature) {
    var marker = new google.maps.Marker({
      position: feature.position,
      icon: icons[feature.type].icon,
      size: new google.maps.Size(5, 5),
      map: map
    });
  }

  var features = [
    {
      position: new google.maps.LatLng(-34.9287, 138.5999),
      type: 'wine'
    },
    {
      position: new google.maps.LatLng(-34.8389, 138.4839),
      type: 'icecream1'
    },
    {
      position: new google.maps.LatLng(-34.9200, 138.4940),
      type: 'icecream2'
    },
    {
      position: new google.maps.LatLng(-34.9795, 138.5101),
      type: 'icecream3'
    }
  ];

  for (var i = 0, feature; feature = features[i]; i++) {
    addMarker(feature);
  }

}
