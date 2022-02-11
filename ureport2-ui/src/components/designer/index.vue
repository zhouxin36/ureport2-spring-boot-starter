<template/>
<script type="text/javascript">
window._server = "http://localhost:9095/ureport";
import designerJs from '../../index.js'
import "../../../public/css/bootstrap.min.css";
let template;
export default {
  mounted: function () {
    designerJs()
  },
  created() {
    template = this.$route.params.template
  }
}
</script>
