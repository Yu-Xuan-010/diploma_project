<script>
export default {
  name: 'RedirectPage', // 修改组件名称
  created() {
    const { params, query } = this.$route
    const { path } = params
    this.$router.replace({ path: '/' + path, query })
  },
  render: function(h) {
    return h() // 避免警告信息
  }
}
</script>
