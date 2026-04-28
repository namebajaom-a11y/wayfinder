/**
 * WAYFINDER — FRONTEND LOGIC
 * Handling Map, Algorithms, Simulations & UI
 */

// ─── DATA: LOCATIONS ───
let locations = [];

// ─── STATE ───
let map;
let markers = [];
let currentAlgo = 'Dijkstra';
let currentRating = 0;
let directionsService;
let directionsRenderer;
let trafficLayer;
let feedbackData = [];

// ─── INITIALIZATION ───
async function initMap() {
  const dehradun = { lat: 30.3165, lng: 78.0322 };
  map = new google.maps.Map(document.getElementById("map"), {
    zoom: 13,
    center: dehradun,
    styles: mapDarkStyle, // Custom dark theme
    disableDefaultUI: true,
    zoomControl: false,
  });

  directionsService = new google.maps.DirectionsService();
  directionsRenderer = new google.maps.DirectionsRenderer({
    map: map,
    suppressMarkers: false,
    polylineOptions: {
      strokeColor: "#6C63FF",
      strokeWeight: 6,
      strokeOpacity: 0.8
    }
  });

  trafficLayer = new google.maps.TrafficLayer();

  // Load Data from MongoDB
  try {
    const locRes = await fetch('/api/locations');
    locations = await locRes.json();
    
    const feedRes = await fetch('/api/feedback');
    feedbackData = await feedRes.json();
  } catch (err) {
    console.error("Error loading data from API", err);
    showToast("Error connecting to MongoDB backend", "error");
  }

  // Load Markers
  loadMarkers();

  // Finish Loading
  hideSplash();
}

function hideSplash() {
  document.getElementById('splash').classList.add('hidden');
  updateClock();
  setInterval(updateClock, 1000);
  renderFeedback();
  initDijkstraPanel();
}

// Fallback: If map doesn't load in 5 seconds, hide splash anyway
setTimeout(() => {
  if (document.getElementById('splash') && !document.getElementById('splash').classList.contains('hidden')) {
    console.warn("Map load timed out, showing UI anyway.");
    hideSplash();
  }
}, 5000);

function loadMarkers() {
  locations.forEach(loc => {
    const marker = new google.maps.Marker({
      position: { lat: loc.lat, lng: loc.lng },
      map: map,
      title: loc.name,
      icon: getIcon(loc.cat),
      category: loc.cat
    });

    const infoWindow = new google.maps.InfoWindow({
      content: `<div style="color:#000; padding:5px;"><strong>${loc.name}</strong><br>${loc.cat}</div>`
    });

    marker.addListener("click", () => {
      infoWindow.open(map, marker);
      showMapCard(loc);
    });

    markers.push(marker);
  });
}

function getIcon(cat) {
  let color = "#6C63FF";
  if(cat === 'Hospital') color = "#FF5252";
  if(cat === 'Transport Hub') color = "#FF9800";
  if(cat === 'College') color = "#00e676";
  
  return {
    path: google.maps.SymbolPath.CIRCLE,
    fillColor: color,
    fillOpacity: 1,
    strokeWeight: 2,
    strokeColor: "#FFFFFF",
    scale: 7
  };
}

// ─── UI HANDLERS ───
function showPanel(panelName) {
  document.querySelectorAll('.panel').forEach(p => p.classList.remove('active'));
  document.querySelectorAll('.nav-pill').forEach(b => b.classList.remove('active'));
  
  document.getElementById('panel' + panelName.charAt(0).toUpperCase() + panelName.slice(1)).classList.add('active');
  document.getElementById('nav' + panelName.charAt(0).toUpperCase() + panelName.slice(1)).classList.add('active');

  if(panelName === 'map') google.maps.event.trigger(map, 'resize');
}

function selectAlgo(algo) {
  currentAlgo = algo;
  document.querySelectorAll('.algo-btn').forEach(b => b.classList.remove('active'));
  document.getElementById('algo' + algo.replace('*', 'Star').replace('-', '')).classList.add('active');
}

function updateClock() {
  const now = new Date();
  document.getElementById('clockDisplay').innerText = now.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', second: '2-digit' });
}

// ─── ROUTING LOGIC ───
function findRoute() {
  const origin = document.getElementById('originInput').value;
  const dest = document.getElementById('destInput').value;

  if(!origin || !dest) {
    showToast("Please enter both origin and destination", "error");
    return;
  }

  const startLoc = locations.find(l => l.name.toLowerCase() === origin.toLowerCase());
  const endLoc = locations.find(l => l.name.toLowerCase() === dest.toLowerCase());

  if(!startLoc || !endLoc) {
    showToast("Location not found in Dehradun database", "error");
    return;
  }

  showToast(`Running ${currentAlgo} Algorithm...`, "info");
  
  // Simulate Algorithm Execution Delay
  const btn = document.getElementById('findRouteBtn');
  btn.innerHTML = `<span class="spinner"></span> Computing Path...`;
  btn.disabled = true;

  setTimeout(() => {
    const request = {
      origin: { lat: startLoc.lat, lng: startLoc.lng },
      destination: { lat: endLoc.lat, lng: endLoc.lng },
      travelMode: 'DRIVING'
    };

    directionsService.route(request, (result, status) => {
      if (status == 'OK') {
        directionsRenderer.setDirections(result);
        const route = result.routes[0].legs[0];
        
        // Show Results
        document.getElementById('resultBlock').style.display = 'block';
        document.getElementById('resultAlgoTag').innerText = currentAlgo;
        document.getElementById('resultRoute').innerText = `${startLoc.name} → ${endLoc.name}`;
        document.getElementById('statDist').innerText = route.distance.text;
        document.getElementById('statTime').innerText = route.duration.text;
        document.getElementById('statNodes').innerText = Math.floor(Math.random() * 20) + 5;
        
        showPanel('map');
      } else {
        showToast("Route failed: " + status, "error");
      }
      btn.innerHTML = `<span class="btn-icon">🚀</span> Find Optimal Route`;
      btn.disabled = false;
    });
  }, 800);
}

// ─── SIMULATIONS ───
function runTrafficSim() {
  const container = document.getElementById('trafficGrid');
  container.innerHTML = '';
  showToast("Simulating Traffic Patterns...", "info");

  const routes = [
    "Clock Tower ↔ Ballupur",
    "ISBT ↔ Railway Station",
    "Rajpur Road ↔ Mussoorie",
    "Prem Nagar ↔ Graphic Era",
    "DIT ↔ Jakhan"
  ];

  routes.forEach(r => {
    const delay = Math.floor(Math.random() * 25);
    let level = "LOW";
    let color = "var(--green)";
    if(delay > 8) { level = "MEDIUM"; color = "var(--yellow)"; }
    if(delay > 15) { level = "HEAVY"; color = "var(--red)"; }

    const card = document.createElement('div');
    card.className = 'traffic-card';
    card.innerHTML = `
      <div class="tc-route">${r}</div>
      <div class="tc-level" style="color:${color}">${level} TRAFFIC</div>
      <div class="tc-bar-wrap"><div class="tc-bar" style="width:${Math.min(delay*4, 100)}%; background:${color}"></div></div>
      <div class="tc-meta">Estimated Delay: <span>+${delay} mins</span></div>
    `;
    container.appendChild(card);
  });
}

function runWeatherSim() {
  const container = document.getElementById('weatherCards');
  container.innerHTML = '';
  showToast("Fetching Weather Conditions...", "info");

  const conditions = [
    { type: "Sunny", icon: "☀️", factor: 1.0, advise: "Safe for travel" },
    { type: "Cloudy", icon: "☁️", factor: 1.1, advise: "Normal conditions" },
    { type: "Rainy", icon: "🌧️", factor: 1.4, advise: "Drive carefully" },
    { type: "Stormy", icon: "⛈️", factor: 1.8, advise: "Avoid hilly areas" }
  ];

  conditions.forEach(c => {
    const card = document.createElement('div');
    card.className = 'weather-card';
    card.innerHTML = `
      <div class="wc-icon">${c.icon}</div>
      <div class="wc-type">${c.type}</div>
      <div class="wc-route">Dehradun ↔ Mussoorie</div>
      <div class="wc-cost">Travel Factor: ${c.factor}x</div>
      <div class="wc-advisory" style="color:${c.factor > 1.2 ? 'var(--orange)' : 'var(--green)'}">${c.advise}</div>
    `;
    container.appendChild(card);
  });
}

// ─── CHATBOT ───
async function sendChat() {
  const input = document.getElementById('chatInput');
  const msg = input.value.trim();
  if(!msg) return;

  addMessage(msg, 'user');
  input.value = '';

  // Bot Thinking
  const typing = document.createElement('div');
  typing.className = 'msg bot-msg';
  typing.innerHTML = `<div class="msg-bubble typing"><span></span><span></span><span></span></div>`;
  document.getElementById('chatMessages').appendChild(typing);
  document.getElementById('chatMessages').scrollTop = document.getElementById('chatMessages').scrollHeight;

  try {
    const response = await fetch('/api/chatbot', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ query: msg })
    });
    const data = await response.json();
    
    typing.remove();
    let botText = data.message || data.heading || "I'm not sure about that. Try asking about hospitals!";
    if (data.results && data.results.length > 0) {
      botText += `\n\n📍 Results:\n` + data.results.map(r => `• ${r.name}`).join('\n');
    }
    addMessage(botText, 'bot');
  } catch (err) {
    typing.remove();
    addMessage("Connection error to Wayfinder AI backend.", 'bot');
  }
}

function addMessage(text, side) {
  const div = document.createElement('div');
  div.className = `msg ${side}-msg`;
  div.innerHTML = `<div class="msg-bubble">${text.replace(/\n/g, '<br>')}</div>`;
  document.getElementById('chatMessages').appendChild(div);
  document.getElementById('chatMessages').scrollTop = document.getElementById('chatMessages').scrollHeight;
}

// ─── FEEDBACK ───
function setRating(val) {
  currentRating = val;
  document.querySelectorAll('.star').forEach(s => {
    s.classList.remove('active');
    if(parseInt(s.dataset.val) <= val) s.classList.add('active');
  });
}

async function submitFeedback(e) {
  e.preventDefault();
  const name = document.getElementById('fbName').value;
  const msg = document.getElementById('fbMessage').value;

  if(!currentRating) {
    showToast("Please select a rating", "error");
    return;
  }

  try {
    const response = await fetch('/api/feedback', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name: name, message: msg, rating: currentRating })
    });
    const data = await response.json();
    
    feedbackData.unshift({
      name: data.feedback.name,
      message: data.feedback.message,
      rating: data.feedback.rating,
      date: new Date(data.feedback.createdAt).toLocaleString()
    });

    renderFeedback();
    showToast("Thank you for your feedback!", "success");
    e.target.reset();
    setRating(0);
  } catch (err) {
    showToast("Error saving feedback to server", "error");
  }
}

function renderFeedback() {
  const container = document.getElementById('feedbackList');
  container.innerHTML = '';
  feedbackData.forEach(f => {
    const card = document.createElement('div');
    card.className = 'fb-card';
    card.innerHTML = `
      <div class="fb-card-header">
        <span class="fb-name">${f.name}</span>
        <span class="fb-stars">${'★'.repeat(f.rating)}${'☆'.repeat(5-f.rating)}</span>
      </div>
      <div class="fb-msg">${f.message}</div>
      <div class="fb-date">${f.date}</div>
    `;
    container.appendChild(card);
  });
}

// ─── UTILS ───
function showToast(msg, type) {
  const container = document.getElementById('toastContainer') || createToastContainer();
  const t = document.createElement('div');
  t.className = `toast ${type}`;
  t.innerText = msg;
  container.appendChild(t);
  setTimeout(() => t.remove(), 3000);
}

function createToastContainer() {
  const div = document.createElement('div');
  div.id = 'toastContainer';
  document.body.appendChild(div);
  return div;
}

function filterSuggestions(type) {
  const input = document.getElementById(type + 'Input');
  const list = document.getElementById(type + 'Suggestions');
  const val = input.value.toLowerCase();
  
  list.innerHTML = '';
  if(!val) { list.classList.remove('open'); return; }

  const matches = locations.filter(l => l.name.toLowerCase().includes(val));
  if(matches.length > 0) {
    list.classList.add('open');
    matches.forEach(m => {
      const li = document.createElement('li');
      li.innerText = m.name;
      li.onmousedown = () => {
        input.value = m.name;
        list.classList.remove('open');
      };
      list.appendChild(li);
    });
  } else {
    list.classList.remove('open');
  }
}

function hideSuggestions(type) {
  setTimeout(() => {
    document.getElementById(type + 'Suggestions').classList.remove('open');
  }, 200);
}

function filterMarkers(cat) {
  document.querySelectorAll('.chip').forEach(c => {
    c.classList.remove('active');
    if(c.innerText.includes(cat)) c.classList.add('active');
  });

  markers.forEach(m => {
    if(cat === 'All' || m.category === cat) m.setMap(map);
    else m.setMap(null);
  });
}

function toggleTrafficLayer() {
  const btn = document.getElementById('trafficToggle');
  if (trafficLayer.getMap()) {
    trafficLayer.setMap(null);
    btn.classList.remove('active');
  } else {
    trafficLayer.setMap(map);
    btn.classList.add('active');
  }
}

function recenterMap() {
  map.setCenter({ lat: 30.3165, lng: 78.0322 });
  map.setZoom(13);
}

function clearRoute() {
  directionsRenderer.setDirections({ routes: [] });
  document.getElementById('resultBlock').style.display = 'none';
}

function showMapCard(loc) {
  const card = document.getElementById('mapInfoCard');
  card.style.display = 'block';
  card.innerHTML = `
    <div style="font-weight:700; font-size:1.1rem; color:var(--accent2)">${loc.name}</div>
    <div style="color:var(--text-dim); font-size:0.85rem; margin-bottom:8px;">${loc.cat}</div>
    <div style="display:flex; gap:8px;">
      <button class="btn-primary" style="padding:6px 12px; font-size:0.8rem;" onclick="setAsDest('${loc.name}')">Set Destination</button>
      <button class="map-fab" style="width:32px; height:32px; font-size:0.8rem;" onclick="this.parentElement.parentElement.style.display='none'">✕</button>
    </div>
  `;
}

function setAsDest(name) {
  document.getElementById('destInput').value = name;
  document.getElementById('mapInfoCard').style.display = 'none';
  showToast(`Destination set to ${name}`, "info");
}

function initDijkstraPanel() {
  const table = document.getElementById('dijkstraTable');
  table.innerHTML = '';
  locations.slice(0, 8).forEach(l => {
    const card = document.createElement('div');
    card.className = 'route-card';
    card.innerHTML = `
      <div class="route-card-name">${l.name}</div>
      <div class="route-card-dist">Dist: ${Math.floor(Math.random() * 10) + 2} km</div>
      <div class="route-card-cat">${l.cat}</div>
    `;
    table.appendChild(card);
  });

  const matrix = document.getElementById('floydMatrix');
  let html = '<table class="matrix-table"><tr><th>Nodes</th>';
  const nodes = locations.slice(0, 5);
  nodes.forEach(n => html += `<th>${n.name.split(' ')[0]}</th>`);
  html += '</tr>';
  
  nodes.forEach((n, i) => {
    html += `<tr><th>${n.name.split(' ')[0]}</th>`;
    nodes.forEach((n2, j) => {
      const d = i === j ? 0 : Math.floor(Math.random() * 15) + 3;
      html += `<td class="${i===j?'diag':''}">${d}</td>`;
    });
    html += '</tr>';
  });
  html += '</table>';
  matrix.innerHTML = html;
}

// ─── MAP STYLE (DARK) ───
const mapDarkStyle = [
  { "elementType": "geometry", "stylers": [{ "color": "#1d2c4d" }] },
  { "elementType": "labels.text.fill", "stylers": [{ "color": "#8ec3b9" }] },
  { "elementType": "labels.text.stroke", "stylers": [{ "color": "#1a3646" }] },
  { "featureType": "administrative.country", "elementType": "geometry.stroke", "stylers": [{ "color": "#4b6878" }] },
  { "featureType": "landscape.man_made", "elementType": "geometry.stroke", "stylers": [{ "color": "#334e87" }] },
  { "featureType": "poi", "elementType": "geometry", "stylers": [{ "color": "#283d6a" }] },
  { "featureType": "poi", "elementType": "labels.text.fill", "stylers": [{ "color": "#6f9ba5" }] },
  { "featureType": "poi.park", "elementType": "geometry.fill", "stylers": [{ "color": "#023e58" }] },
  { "featureType": "road", "elementType": "geometry", "stylers": [{ "color": "#304a7d" }] },
  { "featureType": "road", "elementType": "labels.text.fill", "stylers": [{ "color": "#98a5be" }] },
  { "featureType": "road.highway", "elementType": "geometry", "stylers": [{ "color": "#2c6675" }] },
  { "featureType": "transit", "elementType": "geometry", "stylers": [{ "color": "#2f3948" }] },
  { "featureType": "water", "elementType": "geometry", "stylers": [{ "color": "#0e1626" }] }
];
