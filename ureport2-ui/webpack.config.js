'use strict'
const webpack = require("webpack");
module.exports = {
    presets: [
        '@vue/cli-plugin-babel/preset'
    ],
    configureWebpack: {
        plugins: [
            new webpack.ProvidePlugin({
                $: "jquery",
                jQuery: "jquery",
                "window.jQuery": "jquery",
                "window.$": "jquery",
                Popper: ["popper.js", "default"]
            })
        ]
    },
    devServer: {
        open: true,
        port: '8080',
        disableHostCheck: true,
        host: '0.0.0.0',
        https:  false
    }
}
