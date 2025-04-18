// store/modules/user.js
import axios from 'axios'

export default {
    namespaced: true,
    state: {
        userInfo: JSON.parse(localStorage.getItem('userInfo') || '{}'),
        favoriteCourses: []
    },
    mutations: {
        setUserInfo(state, userInfo) {
            console.log('Vuex mutation setUserInfo 被调用，参数:', userInfo)
            console.log('用户角色值(mutation):', userInfo.role)
            
            // 确保角色值是小写
            const normalizedUserInfo = {
                ...userInfo,
                role: userInfo.role ? userInfo.role.toLowerCase() : 'student'
            }
            
            state.userInfo = normalizedUserInfo
            localStorage.setItem('userInfo', JSON.stringify(normalizedUserInfo))
            console.log('更新后的 state.userInfo:', state.userInfo)
            console.log('更新后的角色值:', state.userInfo.role)
        },
        setFavoriteCourses(state, courses) {
            state.favoriteCourses = courses
        }
    },
    getters: {
        userInfo: state => {
            console.log('获取 userInfo getter，当前值:', state.userInfo)
            console.log('获取 userInfo getter，当前角色值:', state.userInfo?.role)
            return state.userInfo
        },
        isTeacher: state => {
            const role = state.userInfo?.role?.toLowerCase()
            console.log('获取 isTeacher getter，当前角色:', role)
            return role === 'teacher'
        },
        favoriteCourses: state => state.favoriteCourses
    },
    actions: {
        async fetchUserInfo({ commit }) {
            try {
                console.log('开始获取用户信息')
                const response = await axios.get('/api/user/profile', {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem('token')}`
                    }
                })
                
                console.log('获取用户信息响应:', response.data)
                
                if (response.data.success) {
                    commit('setUserInfo', response.data.data)
                    return response.data.data
                } else {
                    console.error('获取用户信息失败:', response.data.message)
                    throw new Error(response.data.message || '获取用户信息失败')
                }
            } catch (error) {
                console.error('获取用户信息出错:', error)
                throw error
            }
        },
        async updateFavoriteCourses({ commit }) {
            try {
                console.log('开始获取收藏课程列表')
                const response = await axios.get('/api/courses/favorites', {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem('token')}`
                    }
                })

                if (response.data.code === 200) {
                    const favoriteRecords = response.data.data
                    console.log('收藏记录:', favoriteRecords)

                    // 用 courseId 批量获取课程详情
                    const courseDetailPromises = favoriteRecords.map(async (favorite) => {
                        try {
                            const courseResponse = await axios.get(`/api/courses/${favorite.courseId}`, {
                                headers: {
                                    Authorization: `Bearer ${localStorage.getItem('token')}`
                                }
                            })
                            
                            if (courseResponse.data.code === 200) {
                                const course = courseResponse.data.data
                                return {
                                    id: course.id,
                                    name: course.name,
                                    description: course.description,
                                    coverImage: course.coverImage || '/default-course.jpg',
                                    teacherName: course.teacherName,
                                    createTime: favorite.createTime // 使用对应收藏记录的创建时间
                                }
                            }
                            return null
                        } catch (error) {
                            console.error(`获取课程 ${favorite.courseId} 详情失败:`, error)
                            return null
                        }
                    })

                    const courses = (await Promise.all(courseDetailPromises)).filter(course => course !== null)
                    console.log('处理后的课程列表:', courses)

                    commit('setFavoriteCourses', courses)
                } else {
                    console.error('获取收藏课程失败:', response.data.message)
                    commit('setFavoriteCourses', [])
                }
            } catch (error) {
                console.error('获取收藏课程出错:', error)
                commit('setFavoriteCourses', [])
            }
        }
    }
}