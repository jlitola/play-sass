play-sass
=========

[Sass][sass] asset handling plugin for [Play 2.0][play]. Implemented as [sbt][sbt]
plugin.

Prerequisites
-------------

[Sass][sass] compiler needs to be installed for plugin to work. This means that `sass` executable
needs to be found in path. Sass can be installed by by installing `sass` gem.

	gem install sass

You can verify that `sass` has been installed by following command:

	  % sass -v
		Sass 3.1.16 (Brainy Betty)


Installation
------------

Add following to your projects `project/plugins.sbt`

	addSbtPlugin("net.litola" % "play-sass" % "0.1.1" from "http://cloud.github.com/downloads/jlitola/play-sass/play-sass-0.1.1.jar")

This adds Sass asset compiler to Play project. `*.sass` and `*.scss` files in `app/assets` 
directories will then be automatically compiled to `*.css` files. Files starting with 
`_`-character will be left out from compilation as per Play convention.

Versions
--------

* *0.1.1* [2012-08-10] Dependency tracking for imported files. Should behave
	correctly with incrementalAssetsCompilation := true. Changed to use play 2.0.3, sbt 0.11.3.
* *0.1.0* [2012-05-04] Initial release

Acknowledgements
----------------

This plugin is based on Johannes Emerich's [play-stylus][play-stylus] plugin for handling 
Stylus assets.

License
-------

Copyright (c) 2012 Juha Litola

MIT-style license, see details from LICENSE file.

[sass]: http://sass-lang.com/
[play]: http://www.playframework.org/
[sbt]: https://github.com/harrah/xsbt
[play-stylus]: https://github.com/knuton/play-stylus
