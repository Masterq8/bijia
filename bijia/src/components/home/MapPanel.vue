<script setup>
import { onMounted, onUnmounted, ref } from 'vue'
import L from 'leaflet'
import '@geoman-io/leaflet-geoman-free'
import 'leaflet/dist/leaflet.css'
import '@geoman-io/leaflet-geoman-free/dist/leaflet-geoman.css'

import marker2x from 'leaflet/dist/images/marker-icon-2x.png'
import marker from 'leaflet/dist/images/marker-icon.png'
import shadow from 'leaflet/dist/images/marker-shadow.png'

delete L.Icon.Default.prototype._getIconUrl
L.Icon.Default.mergeOptions({
  iconRetinaUrl: marker2x,
  iconUrl: marker,
  shadowUrl: shadow,
})

const mapRef = ref()
let mapInstance = null
let drawnPolygonLayer = null

const emit = defineEmits(['draw-created', 'draw-cleared', 'draw-invalid'])

const cityPoints = [
  { name: '北京', lat: 39.9042, lng: 116.4074 },
  { name: '天津', lat: 39.0842, lng: 117.2009 },
  { name: '上海', lat: 31.2304, lng: 121.4737 },
  { name: '南京', lat: 32.0603, lng: 118.7969 },
  { name: '杭州', lat: 30.2741, lng: 120.1551 },
  { name: '广州', lat: 23.1291, lng: 113.2644 },
  { name: '武汉', lat: 30.5928, lng: 114.3055 },
  { name: '西安', lat: 34.3416, lng: 108.9398 },
  { name: '成都', lat: 30.5728, lng: 104.0668 },
  { name: '长春', lat: 43.8171, lng: 125.3235 },
  { name: '沈阳', lat: 41.8057, lng: 123.4315 },
  { name: '哈尔滨', lat: 45.8038, lng: 126.5349 },
]

function addRobotControl() {
  const robotControl = L.control({ position: 'bottomright' })
  robotControl.onAdd = function onAdd() {
    const div = L.DomUtil.create('div', 'leaflet-bar leaflet-control leaflet-control-custom robot-entry')
    div.innerHTML = 'AI'
    div.onclick = function handleClick() {
      // 预留智能问答入口
    }
    return div
  }
  robotControl.addTo(mapInstance)
}

function addCityMarkers(map) {
  cityPoints.forEach((item) => {
    const dot = L.circleMarker([item.lat, item.lng], {
      radius: 4,
      color: '#ff3d3d',
      fillColor: '#ff3d3d',
      fillOpacity: 0.95,
      weight: 1,
    }).addTo(map)

    dot.bindTooltip(item.name, {
      direction: 'top',
      offset: [0, -5],
      permanent: true,
      className: 'city-tooltip',
    })
  })

  const beijingStar = L.divIcon({
    className: 'beijing-star',
    html: '<span>★</span>',
    iconSize: [14, 14],
  })
  L.marker([39.9042, 116.4074], { icon: beijingStar }).addTo(map)
}

function startDrawMode() {
  if (!mapInstance) return
  mapInstance.pm.enableDraw('Polygon', {
    snappable: true,
    snapDistance: 10,
    allowSelfIntersection: false,
  })
}

function cancelDrawMode() {
  if (!mapInstance) return
  mapInstance.pm.disableDraw('Polygon')
}

function clearDrawings() {
  if (!mapInstance) return
  mapInstance.eachLayer((layer) => {
    if (layer.pm && (layer instanceof L.Polygon || layer instanceof L.Rectangle)) {
      mapInstance.removeLayer(layer)
    }
  })
  drawnPolygonLayer = null
  emit('draw-cleared')
}

onMounted(() => {
  mapInstance = L.map(mapRef.value, {
    center: [35.96592727383686, 110.15522837638856],
    zoom: 5,
    zoomControl: false,
    doubleClickZoom: false,
    attributionControl: false,
    minZoom: 2,
    maxZoom: 18,
  })

  L.tileLayer('https://webrd04.is.autonavi.com/appmaptile?lang=zh_cn&size=1&scale=1&style=7&x={x}&y={y}&z={z}', {
    attribution: '',
    maxZoom: 18,
  }).addTo(mapInstance)

  mapInstance.pm.setPathOptions({
    color: '#2f91f3',
    fillColor: '#2f91f3',
    fillOpacity: 0.2,
  })
  mapInstance.pm.setLang('zh')

  mapInstance.on('pm:create', (event) => {
    const latlngs = event.layer.getLatLngs()[0] || []
    if (latlngs.length < 3) {
      mapInstance.removeLayer(event.layer)
      emit('draw-invalid', {
        reason: 'point-count',
        count: latlngs.length,
      })
      return
    }

    if (drawnPolygonLayer) {
      mapInstance.removeLayer(drawnPolygonLayer)
    }
    drawnPolygonLayer = event.layer

    const points = latlngs.map((item) => [item.lng, item.lat])
    emit('draw-created', {
      points,
      bounds: event.layer.getBounds(),
      rawLatLngs: latlngs,
    })

    mapInstance.pm.disableDraw('Polygon')
  })

  mapInstance.on('pm:remove', () => {
    drawnPolygonLayer = null
    emit('draw-cleared')
  })

  addCityMarkers(mapInstance)
  addRobotControl()
})

onUnmounted(() => {
  if (mapInstance) {
    mapInstance.remove()
    mapInstance = null
  }
  drawnPolygonLayer = null
})

defineExpose({
  startDrawMode,
  cancelDrawMode,
  clearDrawings,
})
</script>

<template>
  <div class="map-panel">
    <div ref="mapRef" class="map-view"></div>
  </div>
</template>

<style scoped lang="scss">
.map-panel {
  position: relative;
  width: 100%;
  height: calc(100vh - 54px);

  .map-view {
    width: 100%;
    height: 100%;
    background: #eef2f7;
  }
}

:global(.leaflet-control-custom.robot-entry) {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  background: #fff;
  cursor: pointer;
  user-select: none;
}

:global(.leaflet-tooltip.city-tooltip) {
  color: #333;
  font-size: 14px;
  font-weight: 600;
  border: none;
  border-radius: 3px;
  background: rgba(255, 255, 255, 0.88);
  box-shadow: none;
  padding: 1px 4px;
}

:global(.beijing-star span) {
  color: #ff8a00;
  font-size: 13px;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.4);
}
</style>
