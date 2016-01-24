require 'rails_helper'

RSpec.describe BankAccount, :type => :model do
  it{should have_many :transactions}
  it{should validate_presence_of :name}
  it{should validate_presence_of :currency_code}
end
