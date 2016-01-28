source 'https://rubygems.org'


# Bundle edge Rails instead: gem 'rails', github: 'rails/rails'
gem 'rails', '>= 5.0.0.beta1', '< 5.1'
# Use postgresql as the database for Active Record
gem 'pg', '~> 0.18'
# Build JSON APIs with ease. Read more: https://github.com/rails/jbuilder
# gem 'jbuilder', '~> 2.0'
# Use Puma as the app server
gem 'puma'

# Use ActiveModel has_secure_password
# gem 'bcrypt', '~> 3.1.7'

# Use Capistrano for deployment
# gem 'capistrano-rails', group: :development

# Use Rack CORS for handling Cross-Origin Resource Sharing (CORS), making cross-origin AJAX possible
# gem 'rack-cors'

gem 'active_model_serializers', '~> 0.10.0.rc3'

gem 'money-rails', '~> 1.6'

#gem 'jsonapi-resources', '~> 0.7.0'

group :development, :test do
  # Call 'byebug' anywhere in the code to stop execution and get a debugger console
  gem 'byebug'

  #TODO Temporary fix until rails 5 is released
  gem 'rspec-core', github: 'rspec/rspec-core', branch: 'master'
  gem 'rspec-support', github: 'rspec/rspec-support', branch: 'master'
  gem 'rspec-expectations', github: 'rspec/rspec-expectations', branch: 'master'
  gem 'rspec-mocks', github: 'rspec/rspec-mocks', branch: 'master'
  gem 'rspec-rails', github: 'rspec/rspec-rails', branch: 'master'
  gem 'shoulda', '~> 3.5'
  gem 'factory_girl_rails', '~> 4.5'
  gem 'faker', '~> 1.6', '>= 1.6.1'
  gem 'rails-controller-testing'
end

group :development do
  # Spring speeds up development by keeping your application running in the background. Read more: https://github.com/rails/spring
  #gem 'spring'
end

# Windows does not include zoneinfo files, so bundle the tzinfo-data gem
gem 'tzinfo-data', platforms: [:mingw, :mswin, :x64_mingw, :jruby]
