<template>
  <div ref="parent" class="GameMap">
    <canvas ref="canvas" tabindex="0" style="outline: none;">

    </canvas>
  </div>
</template>

<script>
import { GameMap } from '@/assets/scripts/GameMap.js'
import { ref, onMounted } from 'vue'
import { useStore } from 'vuex'
export default {
  setup() {
    const store = useStore()
    const parent = ref(null)
    const canvas = ref(null)
    onMounted(() => {
      store.commit('updateGameObject', new GameMap(canvas.value.getContext('2d'), parent.value, store))
    })
    return {
      parent,
      canvas,
    }
  },
}
</script>

<style scoped>
div.GameMap {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>