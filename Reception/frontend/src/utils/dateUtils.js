export const dateShortcuts = [
  {
    text: '最近一周',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
      return [start, end]
    },
  },
  {
    text: '最近一个月',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
      return [start, end]
    },
  },
  {
    text: '最近三个月',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
      return [start, end]
    },
  },
]

/**
 * 日期格式化工具函数
 */

// 格式化日期为 YYYY-MM-DD
export const formatDate = (dateString) => {
  if (!dateString) return '暂无数据'
  try {
    const date = new Date(dateString)
    if (isNaN(date.getTime())) {
      return '暂无数据'
    }
    return date.toLocaleDateString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit'
    })
  } catch (error) {
    console.error('日期格式化错误:', error)
    return '暂无数据'
  }
}

// 格式化日期时间为 YYYY-MM-DD HH:mm:ss
export const formatDateTime = (dateTime) => {
  if (!dateTime) return '暂无数据'
  try {
    const date = new Date(dateTime)
    if (isNaN(date.getTime())) {
      return '暂无数据'
    }
    return date.toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit'
    })
  } catch (error) {
    console.error('日期时间格式化错误:', error)
    return '暂无数据'
  }
}

// 格式化日期为API所需的格式 YYYY-MM-DD
export const formatDateForApi = (date) => {
  if (!date) return ''
  try {
    const d = new Date(date)
    if (isNaN(d.getTime())) {
      return ''
    }
    const year = d.getFullYear()
    const month = String(d.getMonth() + 1).padStart(2, '0')
    const day = String(d.getDate()).padStart(2, '0')
    return `${year}-${month}-${day}`
  } catch (error) {
    console.error('API日期格式化错误:', error)
    return ''
  }
} 