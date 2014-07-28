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

Plugin versions are linked to different Play versions. Each Play-version has some differences in how plugin is enabled. Please select suitable instructions below.

After following the instructions `*.sass` and `*.scss` files in `app/assets` 
directories will then be automatically compiled to `*.css` files. Files starting with 
`_`-character will be left out from compilation as per Play convention.


Play 2.3
--------

Add following to your projects `project/plugins.sbt`

	resolvers += "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases"

	addSbtPlugin("net.litola" % "play-sass" % "0.4.0")

After that you'll need to enable plugin in `build.sbt`. 

	lazy val root = (project in file(".")).enablePlugins(PlayScala, net.litola.SassPlugin)

If you would like to pass your own command line arguments to Sass call, you can
do it with `.settings` call. For example to use Compass you should append following after previous line.

	.settings(
    	sassOptions := Seq("--compass")
  	)

Play 2.2
--------

Add following to your projects `project/plugins.sbt`

	resolvers += "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases"

	addSbtPlugin("net.litola" % "play-sass" % "0.3.0")

In addition you'll need to add settings to your project. On Play 2.2 this is
done by modifying `build.sbt` and appending following line:

	net.litola.SassPlugin.sassSettings

If you would like to pass your own command line arguments to Sass call, you can
do it by overriding `SassPlugin.sassOptions`. For example to use Compass you can use
following:

	net.litola.SassPlugin.sassSettings
	
	net.litola.SassPlugin.sassOptions := Seq("--compass")


Play 2.0 & 2.1
--------------

Add following to your projects `project/plugins.sbt`: 

	resolvers += "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases"

	addSbtPlugin("net.litola" % "play-sass" % "0.2.0")

For 2.0 use version 0.1.3.

After that you should do following changes to `project/Build.scala`.

	import net.litola.SassPlugin

	val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings( SassPlugin.sassSettings:_* )


Versions
--------

The newest version only supports Play 2.3. If you need support for older Play versions, please use earlier plugin versions.

* **0.4.0** [2014-07-28] Supports Play 2.3 (Thanks to guofengzh and hrlqn)

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

Copyright (c) 2012-2014 Juha Litola

MIT-style license, see details from LICENSE file.

[sass]: http://sass-lang.com/
[play]: http://www.playframework.org/
[sbt]: https://github.com/harrah/xsbt
[play-stylus]: https://github.com/knuton/play-stylus
