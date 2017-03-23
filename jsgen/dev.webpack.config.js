var webpack = require('webpack');

module.exports = require('./scalajs.webpack.config');
module.exports.module = {
    rules: [
        {
            test: /\.css$/,
            use: [ 'style-loader', 'css-loader' ]
        }
    ]
};
module.exports.plugins = (module.exports.plugins || []);
