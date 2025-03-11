import Vue from 'vue'
import Vuex from 'vuex'
import user from './modules/user'
import course from './modules/course'
import learning from './modules/learning'
import interaction from './modules/interaction'
import getters from './getters'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    user,
    course,
    learning,
    interaction
  },
  getters
})

export default store
