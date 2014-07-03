play-sass
=========

[Sass][sass] asset handling plugin for [Play 2.x][play]. Implemented as [sbt][sbt]
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

	resolvers += "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases"

	addSbtPlugin("net.litola" % "play-sass" % "0.4.0")

In addition you'll need to add settings to your project. On Play 2.2 this is
done by modifying `build.sbt` and adding import for the SassPlugin, and adding
SassPlugin settings.

	import net.litola.SassPlugin

	play.Project.playScalaSettings ++ SassPlugin.sassSettings

On Play 2.1 and Play 2.0 you should do following changes to `project/Build.scala`.

	import net.litola.SassPlugin

	val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings( SassPlugin.sassSettings:_* )

This adds Sass asset compiler to Play project. `*.sass` and `*.scss` files in `app/assets` 
directories will then be automatically compiled to `*.css` files. Files starting with 
`_`-character will be left out from compilation as per Play convention.

Customizing
-----------

If you would like to pass your own command line arguments to Sass call, you can
do it by overriding `Sassplugin.sassOptions`. For example to use Compass you can use
following:

	play.Project.playScalaSettings ++ SassPlugin.sassSettings ++ Seq(SassPlugin.sassOptions := Seq("--compass", "-r", "compass"))

Versions
--------

The newest version only support Play 2.3. If you need support for older versions, please use
0.3.0, 0.2.x or 0.1.x series.
* **0.4.0** [2014-07-03] Supports Play 2.3
* **0.3.0** [2013-09-25] Supports Play 2.2 (Thanks to Nilanjan Raychaudhuri and
	Zarkus13)
* **0.2.0** [2013-03-01] Supports Play 2.1
* **0.1.3** [2013-02-04] Sass command line options can be overridden. Do not
	override settings in plugin (Thanks to Kenji Yoshida). Made play-sass
	available via Sonatype.
* **0.1.2** [2012-11-16] Minimal windows support by Kalle Bertell. Changed to use
	play 2.0.4.
* **0.1.1** [2012-08-10] Dependency tracking for imported files. Should behave
	correctly with incrementalAssetsCompilation := true. Changed to use play 2.0.3, sbt 0.11.3.
* **0.1.0** [2012-05-04] Initial release

Acknowledgements
----------------

This plugin is based on Johannes Emerich's [play-stylus][play-stylus] plugin for handling 
Stylus assets.

License
-------

Copyright (c) 2012-2013 Juha Litola

MIT-style license, see details from LICENSE file.

[sass]: http://sass-lang.com/
[play]: http://www.playframework.org/
[sbt]: https://github.com/harrah/xsbt
[play-stylus]: https://github.com/knuton/play-stylus
