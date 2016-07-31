
// get events
// var events = require("events.json");

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
    },
    event: {
      icon: 'icons/event_red.svg'
    }
  };


  function addMarker(feature) {
    var marker = new google.maps.Marker({
      position: feature.position,
      icon: icons[feature.type].icon,
      size: new google.maps.Size(5, 5),
      map: map
    });

    var panel =
      feature.content + '<br>' +
      '<i class="fa fa-twitter" aria-hidden="true"></i>' + 5 + '<br>' +
      '<i class="fa fa-instagram" aria-hidden="true"></i>' + 5 + '<br>';


    var iw = new google.maps.InfoWindow({
      content: panel
    });
    google.maps.event.addListener(marker, "click", function (e) {
      iw.open(map, this);
    });
  }

  var features = [
    {
      position: new google.maps.LatLng(-34.9287, 138.5999),
      type: 'wine',
      content: 'Wine City Party'
    },
    {
      position: new google.maps.LatLng(-34.8389, 138.4839),
      type: 'icecream1',
      content: 'Royal Copenhagen Semaphore'
    },
    {
      position: new google.maps.LatLng(-34.9200, 138.4940),
      type: 'icecream2',
      content: 'Royal Copenhagen Ice Cream Cone Co'
    },
    {
      position: new google.maps.LatLng(-34.9795, 138.5101),
      type: 'icecream3',
      content: "Andersen's of Denmark Ice Cream"
    }
  ];

  events.forEach( function(e) {
    if (e.loc && e.loc.latitude && e.loc.longitude) {
      let f = {};
      f.position = new google.maps.LatLng(e.loc.latitude, e.loc.longitude);
      f.type = 'event';
      f.content = e.title;
      features.push(f);
    }
  });

  for (var i = 0, feature; feature = features[i]; i++) {
    addMarker(feature);
  }

}
