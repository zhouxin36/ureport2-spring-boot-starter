<template style="margin:5px">
</template>
<script type="text/javascript">
  window._server = "http://localhost:9095/ureport";
  import "../../../public/css/bootstrap.min.css";
  import {submitSearchForm,previewInit} from '@/preview';
  window.formElements = [];

  window.doSearch = function doSearch() {
    submitSearchForm();
  }
  export default {
    mounted: function () {
      previewInit()
    }
  }
</script>
